package teamficial.teamficial_be.domain.recruitingPost.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamficial.teamficial_be.domain.profile.entity.Profile;
import teamficial.teamficial_be.domain.profile.repository.ProfileRepository;
import teamficial.teamficial_be.domain.recruitingDetail.entity.RecruitingDetail;
import teamficial.teamficial_be.domain.recruitingDetail.repository.RecruitingDetailRepository;
import teamficial.teamficial_be.domain.recruitingPost.dto.RecruitingPostDTO;
import teamficial.teamficial_be.domain.recruitingPost.entity.ProgressWay;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingStatus;
import teamficial.teamficial_be.domain.recruitingPost.repository.RecruitingPostRepository;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.domain.user.repository.UserRepository;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;
import teamficial.teamficial_be.global.apiPayload.exception.handler.NotFoundHandler;
import teamficial.teamficial_be.global.enums.Position;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RecruitingPostService {

    private final UserRepository userRepository;
    private final RecruitingPostRepository recruitingPostRepository;
    private final ProfileRepository profileRepository; //프로필 여러개 있을때 고르는 과정 때문에 필요
    private final RecruitingDetailRepository recruitingDetailRepository;


    @Transactional
    public RecruitingPostDTO.RecruitingPostResponseDTO createPost(Long userId, RecruitingPostDTO.RecruitingPostRequestDTO dto) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_USER));

        Profile profile = profileRepository.findById(dto.getProfileId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로필 ID입니다."));

        RecruitingPost post = RecruitingPost.builder()
                .profile(profile)
                .progressWay(dto.getProgressWay())
                .contactWay(dto.getContactWay())
                .startDate(dto.getStartDate())
                .period(dto.getPeriod())
                .deadline(dto.getDeadline())
                .status(dto.getStatus())
                .content(dto.getContent())
                .title(dto.getTitle())
                .build();

        RecruitingPost saved = recruitingPostRepository.save(post);

        List<RecruitingDetail> details = dto.getRecruitingPositions().stream()
                .map(detailDto -> RecruitingDetail.builder()
                        .recruitingPost(saved)
                        .position(detailDto.getPosition())
                        .count(detailDto.getCount())
                        .build())
                .toList();

        recruitingDetailRepository.saveAll(details);


        return RecruitingPostDTO.RecruitingPostResponseDTO.from(saved, details);
    }

    public RecruitingPostDTO.RecruitingPostDeleteResponseDTO deletePost(Long userId, Long postId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_USER));

        RecruitingPost post = recruitingPostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_RECRUITING_POST));
        recruitingPostRepository.delete(post);

        return RecruitingPostDTO.RecruitingPostDeleteResponseDTO.of(postId, "게시글이 성공적으로 삭제되었습니다.");

    }


    public RecruitingPost getRecruitingPostById(Long recruitingPostId) {
        return recruitingPostRepository.findById(recruitingPostId)
                .orElseThrow(()-> new NotFoundHandler(ErrorStatus.NOT_FOUND_RECRUITING_POST));
    }


    @Transactional
    public RecruitingPostDTO.RecruitingPostModifyResponseDTO updatePost(Long userId, Long postId, RecruitingPostDTO.RecruitingPostModifyRequestDTO dto) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_USER));

        RecruitingPost post = recruitingPostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_RECRUITING_POST));

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

        return RecruitingPostDTO.RecruitingPostModifyResponseDTO.from(
                post, recruitingDetailRepository.findByRecruitingPostId(postId)
        );

    }

    @Transactional(readOnly = true)
    public RecruitingPostDTO.RecruitingPostResponseDTO getPost(Long userId, Long postId) {

        userRepository.findById(userId).orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_USER));

        RecruitingPost post = recruitingPostRepository.findByIdWithProfile(postId)
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_RECRUITING_POST));

        List<RecruitingDetail> recruitingDetails =
                recruitingDetailRepository.findByRecruitingPostId(postId);

        return RecruitingPostDTO.RecruitingPostResponseDTO.from(post, recruitingDetails);
    }

    public void validatePostOwner(User user, RecruitingPost recruitingPost) {
        Long writerId = recruitingPost.getProfile().getUser().getId();
        if (!user.getId().equals(writerId)) {
            throw new GeneralException(ErrorStatus._FORBIDDEN);
        }
    }

    public Page<RecruitingPost> getAllRecruitingPostsByUserAndStatus(Long userId, Pageable pageable,RecruitingStatus recruitingStatus) {
        if (recruitingStatus ==null){
            return recruitingPostRepository.findAllByProfile_User_Id(userId,pageable);
        } else {
            return recruitingPostRepository.findAllByProfile_User_IdAndStatus(userId,pageable,recruitingStatus);
        }
    }


    public Page<RecruitingPostDTO.RecruitingPostResponseDTO> getRecruitingPosts(
            RecruitingStatus status,
            Position position,
            ProgressWay progressWay,
            Pageable pageable) {

        Page<RecruitingPost> posts =
                recruitingPostRepository.findByFilters(status, position, progressWay, pageable);

        return posts.map(RecruitingPostDTO.RecruitingPostResponseDTO::from);
    }
}
