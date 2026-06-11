package teamficial.teamficial_be.domain.recruitingPost.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamficial.teamficial_be.domain.application.service.ApplicationService;
import teamficial.teamficial_be.domain.profile.entity.Profile;
import teamficial.teamficial_be.domain.profile.repository.ProfileRepository;
import teamficial.teamficial_be.domain.profile.service.PreSignedUrlService;
import teamficial.teamficial_be.domain.recruitingDetail.entity.RecruitingDetail;
import teamficial.teamficial_be.domain.recruitingDetail.repository.RecruitingDetailRepository;
import teamficial.teamficial_be.domain.recruitingPost.dto.RecruitingPostDto;
import teamficial.teamficial_be.domain.recruitingPost.dto.RecruitingPostPagingDto;
import teamficial.teamficial_be.domain.recruitingPost.dto.response.PostImageResponseDto;
import teamficial.teamficial_be.domain.recruitingPost.entity.PostImage;
import teamficial.teamficial_be.domain.recruitingPost.entity.ProgressWay;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingStatus;
import teamficial.teamficial_be.domain.recruitingPost.repository.RecruitingPostRepository;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.domain.user.repository.UserRepository;
import teamficial.teamficial_be.domain.user.service.UserService;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;
import teamficial.teamficial_be.global.apiPayload.exception.handler.NotFoundHandler;
import teamficial.teamficial_be.global.enums.Position;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecruitingPostService {

    private final UserRepository userRepository;
    private final RecruitingPostRepository recruitingPostRepository;
    private final ProfileRepository profileRepository;
    private final RecruitingDetailRepository recruitingDetailRepository;
    private final ApplicationService applicationService;
    private final UserService userService;
    private final PreSignedUrlService preSignedUrlService;
    private final PostImageService postImageService;


    @Transactional
    public RecruitingPostDto.RecruitingPostsResponseDTO createPost(Long userId, RecruitingPostDto.RecruitingPostRequestDTO dto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_USER));

        Profile profile = profileRepository.findById(dto.getProfileId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로필 ID입니다."));

        RecruitingPost post = RecruitingPost.builder()
                .profile(profile)
                .user(user)
                .progressWay(dto.getProgressWay())
                .contactWay(dto.getContactWay())
                .startDate(dto.getStartDate())
                .period(dto.getPeriod())
                .deadline(dto.getDeadline())
                .status(dto.getStatus())
                .content(dto.getContent())
                .title(dto.getTitle())
                .build();

        long dDay = post.getDDay();
        RecruitingPost saved = recruitingPostRepository.save(post);

        List<RecruitingDetail> details = dto.getRecruitingPositions().stream()
                .map(detailDto -> RecruitingDetail.builder()
                        .recruitingPost(saved)
                        .position(detailDto.getPosition())
                        .count(detailDto.getCount())
                        .build())
                .toList();

        recruitingDetailRepository.saveAll(details);

        // 게시글 이미지 처리
        if (dto.getImageKeys() != null && !dto.getImageKeys().isEmpty()) {
            postImageService.saveImages(saved, dto.getImageKeys());
        }

        return RecruitingPostDto.RecruitingPostsResponseDTO.from(saved, details, dDay);
    }


    public RecruitingPostDto.RecruitingPostDeleteResponseDTO deletePost(Long userId, Long postId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_USER));

        RecruitingPost post = recruitingPostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_RECRUITING_POST));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new GeneralException(ErrorStatus._FORBIDDEN); // 권한 없음
        }

        // S3 이미지 삭제
        postImageService.deleteImagesByPostId(postId);

        recruitingPostRepository.delete(post);

        return RecruitingPostDto.RecruitingPostDeleteResponseDTO.of(postId, "게시글이 성공적으로 삭제되었습니다.");

    }


    public RecruitingPost getRecruitingPostById(Long recruitingPostId) {
        return recruitingPostRepository.findById(recruitingPostId)
                .orElseThrow(()-> new NotFoundHandler(ErrorStatus.NOT_FOUND_RECRUITING_POST));
    }

    @Transactional(readOnly = true)
    public RecruitingPost getRecruitingPostWithDetails(Long recruitingPostId) {
        return recruitingPostRepository.findWithDetailsById(recruitingPostId)
                .orElseThrow(() ->
                        new NotFoundHandler(ErrorStatus.NOT_FOUND_RECRUITING_POST)
                );
    }



    @Transactional
    public RecruitingPostDto.RecruitingPostModifyResponseDTO updatePost(Long userId, Long postId, RecruitingPostDto.RecruitingPostModifyRequestDTO dto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_USER));

        RecruitingPost post = recruitingPostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_RECRUITING_POST));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new GeneralException(ErrorStatus._FORBIDDEN); // 권한 없음
        }

        post.update(dto);

        // 모집 직무/인원(recruitingPositions) 비교·갱신
        if (dto.getRecruitingPositions() != null && !dto.getRecruitingPositions().isEmpty()) {
            List<RecruitingDetail> existingDetails =
                    recruitingDetailRepository.findByRecruitingPostId(postId);

            dto.getRecruitingPositions().forEach(req -> {
                RecruitingDetail existing = existingDetails.stream()
                        .filter(d -> d.getPosition().equals(req.getPosition()))
                        .findFirst()
                        .orElse(null);

                if (existing != null) {
                    if (!Objects.equals(existing.getCount(), req.getCount())) {
                        existing.updateCount(req.getCount());
                    }
                } else {
                    RecruitingDetail newDetail = RecruitingDetail.builder()
                            .recruitingPost(post)
                            .position(req.getPosition())
                            .count(req.getCount())
                            .build();
                    recruitingDetailRepository.save(newDetail);
                }
            });
        }

        if (dto.getImageKeys() != null) {
            postImageService.updatePostImages(post, dto.getImageKeys());
        }

        List<String> imageUrls = postImageService.getPostImageUrls(postId);

        return RecruitingPostDto.RecruitingPostModifyResponseDTO.from(
                post, imageUrls, recruitingDetailRepository.findByRecruitingPostId(postId)
        );

    }

    @Transactional(readOnly = true)
    public RecruitingPostDto.RecruitingPostDetailResponseDTO getPost(Long postId,Long userId) {

        RecruitingPost post = recruitingPostRepository.findByIdWithProfile(postId)
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_RECRUITING_POST));

        long dDay = post.getDDay();

        List<RecruitingDetail> recruitingDetails =
                recruitingDetailRepository.findByRecruitingPostId(postId);

        List<PostImageResponseDto> images =
                postImageService.getPostImages(postId);

        boolean alreadyApplied = false;
        boolean isWriter = false;

        if (userId !=null){
            User user = userService.getUserById(userId);

            alreadyApplied = applicationService.alreadyApplied(userId,postId);
            isWriter = post.isWriter(user);
        }

        return RecruitingPostDto.RecruitingPostDetailResponseDTO.from(post, recruitingDetails, images, dDay, alreadyApplied,isWriter);
    }

    public void validatePostOwner(User user, RecruitingPost recruitingPost) {
        Long writerId = recruitingPost.getUser().getId();
        if (!user.getId().equals(writerId)) {
            throw new GeneralException(ErrorStatus._FORBIDDEN);
        }
    }

    public boolean isPostOwner(User user, RecruitingPost recruitingPost) {
        Long writerId = recruitingPost.getUser().getId();

        return user.getId().equals(writerId);
    }

    public Page<RecruitingPost> getAllRecruitingPostsByUserAndStatus(User user, Pageable pageable,RecruitingStatus recruitingStatus) {
        if (recruitingStatus == null){
            return recruitingPostRepository.findAllByUser(user,pageable);
        } else {
            return recruitingPostRepository.findAllByUserAndStatus(user,pageable,recruitingStatus);
        }
    }

    public Page<RecruitingPostDto.RecruitingPostsResponseDTO> getRecruitingPosts(
            RecruitingStatus status,
            Position position,
            ProgressWay progressWay,
            Pageable pageable) {

        Page<RecruitingPostPagingDto.RecruitingPostFlatDto> flatPosts =
                recruitingPostRepository.findByFilters(status, position, progressWay, pageable);

        List<Long> postIds = flatPosts.getContent().stream()
                .map(RecruitingPostPagingDto.RecruitingPostFlatDto::getPostId)
                .toList();

        List<RecruitingPostPagingDto.RecruitingDetailFlatDto> flatPostDetails =
                recruitingPostRepository.findPositionsByPostIds(postIds);

        Map<Long, List<RecruitingPostDto.RecruitingPositionDto>> positionMap =
                flatPostDetails.stream()
                        .collect(Collectors.groupingBy(
                                RecruitingPostPagingDto.RecruitingDetailFlatDto::getPostId,
                                Collectors.mapping(
                                        p -> new RecruitingPostDto.RecruitingPositionDto(
                                                p.getPosition(),
                                                p.getCount()
                                        ),
                                        Collectors.toList()
                                )
                        ));

        List<RecruitingPostDto.RecruitingPostsResponseDTO> result =
                flatPosts.stream()
                        .map(post -> {
                            List<RecruitingPostDto.RecruitingPositionDto> positions =
                                    positionMap.getOrDefault(post.getPostId(), Collections.emptyList());

                            long dDay = RecruitingPost.checkDDay(post.getDeadline());

                            return RecruitingPostDto.RecruitingPostsResponseDTO.from(
                                    post,
                                    positions,
                                    dDay
                            );
                        })
                        .toList();

        return new PageImpl<>(result, pageable, flatPosts.getTotalElements());

    }

    public List<RecruitingPost> getRecruitingPostsByProfile(Profile profile) {
        return recruitingPostRepository.findAllByProfile(profile);
    }

    public List<RecruitingPost> getAllByUser(User user) {
        return recruitingPostRepository.findAllByUser(user);
    }

    public List<RecruitingPost> getTop3ByUserOrderByDeadlineAsc(User user) {
        return recruitingPostRepository.findTop3ByUserOrderByDeadlineAsc(user);
    }

    public List<RecruitingPost> getAllRecruitingPostsByUserAndStatus(User user, RecruitingStatus recruitingStatus) {
        return recruitingPostRepository.findAllByUserAndStatus(user,recruitingStatus);
    }
}
