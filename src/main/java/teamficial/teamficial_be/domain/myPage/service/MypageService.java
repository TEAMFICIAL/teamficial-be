package teamficial.teamficial_be.domain.myPage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamficial.teamficial_be.domain.application.dto.response.ApplicationResponseDto;
import teamficial.teamficial_be.domain.application.entity.Application;
import teamficial.teamficial_be.domain.application.entity.ApplicationStatus;
import teamficial.teamficial_be.domain.application.service.ApplicationService;
import teamficial.teamficial_be.domain.confirmed.service.ConfirmedProfileService;
import teamficial.teamficial_be.domain.keyword.service.HeadKeywordService;
import teamficial.teamficial_be.domain.myPage.dto.response.*;
import teamficial.teamficial_be.domain.profile.entity.Profile;
import teamficial.teamficial_be.domain.profile.service.ProfileService;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingStatus;
import teamficial.teamficial_be.domain.recruitingPost.service.RecruitingPostService;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;
import teamficial.teamficial_be.global.enums.Position;
import teamficial.teamficial_be.global.redis.RedisService;
import teamficial.teamficial_be.global.util.PagedResponse;

import java.util.*;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class MypageService {

    private final RecruitingPostService recruitingPostService;
    private final ApplicationService applicationService;
    private final RedisService redisService;
    private final ProfileService profileService;
    private final ConfirmedProfileService confirmedProfileService;
    private final HeadKeywordService headKeywordService;


    @Transactional(readOnly = true)
    public PagedResponse<MyApplicationResponseDto> getAllApplications(User user, int page, int size,ApplicationStatus applicationStatus) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "recruitingPost.createdAt"));

        Page<Application> applicationPage = null;

        if (applicationStatus == ApplicationStatus.MATCHING){
            applicationPage = applicationService.getApplicationsByUserAndStatusIn(user,pageable,List.of(ApplicationStatus.MATCHING, ApplicationStatus.TEMP_SAVED));
        } else {
            applicationPage = applicationService.getApplicationsByUserAndStatus(user,pageable,applicationStatus);
        }

        Page<MyApplicationResponseDto> dtoPage = applicationPage
                .map(application -> {
                    RecruitingPost recruitingPost = application.getRecruitingPost();
                    ApplicationStatus status = application.getApplicationStatus();

                    if (recruitingPost.getStatus() == RecruitingStatus.OPEN || status ==ApplicationStatus.TEMP_SAVED) {
                        return MyApplicationResponseDto.of(application.getRecruitingPost(), ApplicationStatus.MATCHING.getDescription());
                    }

                    return MyApplicationResponseDto.of(application.getRecruitingPost(), status.getDescription());
                });

        return PagedResponse.of(dtoPage);
    }

    @Transactional(readOnly = true)
    public PagedResponse<CurrentApplicantResponseDto> getAllCurrentApplication(User user, int page, int size, RecruitingStatus recruitingStatus) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "deadline"));

        Page<RecruitingPost> recruitingPostPage = recruitingPostService.getAllRecruitingPostsByUserAndStatus(user,pageable,recruitingStatus);

        List<Long> postIds = recruitingPostPage.stream()
                .map(RecruitingPost::getId)
                .toList();

        Map<Long,Integer> applicantCountMap = getApplicantCountsBatch(postIds);

        Page<CurrentApplicantResponseDto> dtoPage = recruitingPostPage.map(recruitingPost -> {
            long dDay = recruitingPost.getDDay();
            //int totalApplicants = getApplicantCount(recruitingPost);
            int totalApplicants = applicantCountMap.get(recruitingPost.getId());
            return CurrentApplicantResponseDto.of(recruitingPost,dDay,totalApplicants);
        });

        return PagedResponse.of(dtoPage);
    }

    private Map<Long, Integer> getApplicantCountsBatch(List<Long> postIds) {
        List<String> keys = postIds.stream()
                .map(redisService::applicationKey)
                .toList();

        List<Object> cachedValues = redisService.getValues(keys);
        log.debug("cachedValues: {}", cachedValues);

        Map<Long,Integer> resultMap = new HashMap<>();
        List<Long> missIds = new ArrayList<>();

        for (int i=0; i < postIds.size(); i++) {
            Long postId = postIds.get(i);
            String key = keys.get(i);
            Object raw = cachedValues.get(i);
            if (raw == null) {
                // 캐시가 없는 경우
                log.debug("캐시 미스 key={}, postId={}", key, postId);
                missIds.add(postId);
                continue;
            }
            String value = raw.toString().trim();

            // 만약 value가 큰따옴표로 감싸져 있다면 제거
            if (value.startsWith("\"") && value.endsWith("\"") && value.length() > 1) {
                value = value.substring(1, value.length()-1);
            }

            if (!value.isEmpty()) {
                int count = Integer.parseInt(value);
                resultMap.put(postId, count);
                log.debug("캐시 히트 key={}, value={}, postId={}", key, raw, postId);
            } else {
                log.debug("캐시 미스 key={}, postId={}", key, postId);
                missIds.add(postId);
            }
        }

        if (!missIds.isEmpty()) {
            log.debug("DB 조회 대상 모집글들: {}", missIds);
            Map<Long, Integer> dbCounts = applicationService.getApplicantCountBatch(missIds);

            Map<String, String> cacheData = new HashMap<>();
            dbCounts.forEach((postId,count) -> {
                resultMap.put(postId, count);
                cacheData.put(redisService.applicationKey(postId), String.valueOf(count));
            });

            if (!cacheData.isEmpty()) {
                redisService.setValues(cacheData);
                log.debug("캐시 일괄 저장 완료 keys={}", cacheData.keySet());
            }
        }

        return resultMap;
    }

    @Transactional(readOnly = true)
    public CurrentApplicationDetailResponseDto getCurrentApplication(Long recruitingPostId, User user, Position position) {

        RecruitingPost recruitingPost = recruitingPostService.getRecruitingPostById(recruitingPostId);
        log.info("모집 글 id: " + recruitingPost.getId());
        recruitingPostService.validatePostOwner(user,recruitingPost);

        long dDay = recruitingPost.getDDay();

        List<Application> applications = applicationService.getApplicationsByRecruitingPost(recruitingPost);

        if (position != null) {
            applications = applications.stream()
                    .filter(application -> application.getPosition() == position)
                    .toList();
        }
        recruitingPost.getDDay();


        return CurrentApplicationDetailResponseDto.from(recruitingPost, applications, dDay);
    }

    @Transactional
    public void closedApplication(User user, Long recruitingPostId) {
        RecruitingPost recruitingPost = recruitingPostService.getRecruitingPostById(recruitingPostId);

        recruitingPostService.validatePostOwner(user, recruitingPost);

        recruitingPost.closedRecruitingPost();
        List<Application> applications = applicationService.getApplicationsByRecruitingPost(recruitingPost);
        applications.stream()
                .filter(app -> app.getApplicationStatus() == ApplicationStatus.MATCHING
                        || app.getApplicationStatus() == ApplicationStatus.TEMP_SAVED)
                .forEach(app -> {
                    if (app.getApplicationStatus() == ApplicationStatus.TEMP_SAVED) {
                        app.updateStatus(ApplicationStatus.MATCHED);
                    } else {
                        app.updateStatus(ApplicationStatus.MATCH_FAILED);
                    }

                    if (app.getApplicationStatus() == ApplicationStatus.MATCHED) {
                        Profile profile = profileService.getProfileWithLinks(app.getProfile().getId());
                        profile.getHeadKeywords().size();

                        confirmedProfileService.createSnapshotFrom(profile, app.getPosition(), recruitingPost);
                    }

                });

        applicationService.saveApplications(applications);
    }

    @Transactional(readOnly = true)
    public ApplicationResponseDto getApplicantProfile(User user, Long recruitingPostId,Long applicationId) {
        RecruitingPost recruitingPost = recruitingPostService.getRecruitingPostById(recruitingPostId);
        recruitingPostService.validatePostOwner(user, recruitingPost);

        Application application = applicationService.getApplication(applicationId);
        if (!application.getRecruitingPost().getId().equals(recruitingPostId)) {
            throw new GeneralException(ErrorStatus._FORBIDDEN);
        }

        return ApplicationResponseDto.from(application);
    }

    @Transactional
    public void confirmedApplicant(User user, Long recruitingPostId, Long applicationId,ApplicationStatus applicationStatus) {
        RecruitingPost recruitingPost = recruitingPostService.getRecruitingPostById(recruitingPostId);
        recruitingPostService.validatePostOwner(user, recruitingPost);

        Application application = applicationService.getApplication(applicationId);
        if (!application.getRecruitingPost().getId().equals(recruitingPostId)) {
            throw new GeneralException(ErrorStatus._FORBIDDEN);
        }

        application.updateStatus(applicationStatus);
        applicationService.saveApplication(application);
    }

    @Transactional(readOnly = true)
    public DashboardResponseDto getUserDashBoard(User user) {
        List<RecruitingPost> recruitingPostList = recruitingPostService.getAllByUser(user);
        List<Application> applicationList = applicationService.getAllByUser(user);

        List<Long> postIds = recruitingPostList.stream()
                .map(RecruitingPost::getId)
                .toList();

        Map<Long,Integer> applicantCountMap = getApplicantCountsBatch(postIds);

        List<CurrentApplicantResponseDto> recruitingDtos = recruitingPostList.stream()
                .limit(3)
                .map(recruitingPost -> {
                    long dDay = recruitingPost.getDDay();
                    int totalApplicants = applicantCountMap.get(recruitingPost.getId());
                    return CurrentApplicantResponseDto.of(recruitingPost, dDay, totalApplicants);
                })
                .toList();

        List<MyApplicationResponseDto> applicationDtos = applicationList.stream()
                .limit(3)
                .map(app -> {
                    ApplicationStatus status = app.getApplicationStatus();
                    if (status == ApplicationStatus.TEMP_SAVED) {
                        return MyApplicationResponseDto.of(app.getRecruitingPost(), ApplicationStatus.MATCHING.getDescription());
                    }
                    return MyApplicationResponseDto.of(app.getRecruitingPost(), app.getApplicationStatus().getDescription());
                })
                .toList();

        //지원한 내역 중 MATCHED 상태인 것들
        List<MyTeamResponseDto> matchedList = applicationList.stream()
                .filter(app -> app.getApplicationStatus() == ApplicationStatus.MATCHED)
                .map(app -> {
                    int totalMembers = confirmedProfileService.getTotalMembers(app.getRecruitingPost());
                    return MyTeamResponseDto.of(app.getRecruitingPost(), totalMembers);
                })
                .toList();

        //내가 작성한 모집글 중 마감된 것 → MyTeamResponseDto 변환
        List<MyTeamResponseDto> closedPostList = recruitingPostList.stream()
                .filter(post -> post.getStatus() == RecruitingStatus.CLOSED)
                .map(post -> {
                    int totalMembers = confirmedProfileService.getTotalMembers(post);
                    return MyTeamResponseDto.of(post, totalMembers);
                })
                .toList();

        //두 리스트를 합치고 최대 3개까지 (최신순)
        List<MyTeamResponseDto> myTeamResponseDtos = Stream.concat(matchedList.stream(), closedPostList.stream())
                .sorted(Comparator.comparing( MyTeamResponseDto::getCreateAt).reversed())
                .limit(3)
                .toList();

        return DashboardResponseDto.builder()
                .myRecruitingPost(recruitingDtos)
                .myApplications(applicationDtos)
                .myTeamResponses(myTeamResponseDtos)
                .build();
    }

    @Transactional(readOnly = true)
    public PagedResponse<MyTeamResponseDto> getMyTeams(User user, int page, int size) {

        //지원한 내역 중 MATCHED 상태인 것들의 게시글
        List<RecruitingPost> myMatchedPostList = applicationService.getAllByUserAndStatus(user, ApplicationStatus.MATCHED).stream()
                .map(Application::getRecruitingPost)
                .toList();

        //내가 작성한 모집글 중 마감된 것
        List<RecruitingPost> recruitingPostList = recruitingPostService.getAllRecruitingPostsByUserAndStatus(user,RecruitingStatus.CLOSED);

        //두 리스트 합치고 모집 글 최신순으로 페이징
        List<MyTeamResponseDto> posts = new ArrayList<>();

        myMatchedPostList.forEach(post -> {
            int totalMembers = confirmedProfileService.getTotalMembers(post);
            List<String> tags = post.getRecruitingDetails().stream()
                    .map(recruitingDetail -> recruitingDetail.getPosition().getDescription())
                    .toList();
            posts.add(MyTeamResponseDto.from(post,totalMembers, tags));
        });

        recruitingPostList.forEach(post -> {
            int totalMembers = confirmedProfileService.getTotalMembers(post);
            List<String> tags = post.getRecruitingDetails().stream()
                    .map(recruitingDetail -> recruitingDetail.getPosition().getDescription())
                    .toList();
            posts.add(MyTeamResponseDto.from(post,totalMembers, tags));
        });

        posts.sort(Comparator.comparing( MyTeamResponseDto::getCreateAt).reversed());


        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), posts.size());
        List<MyTeamResponseDto> pageContent = (start <= end) ? posts.subList(start, end) : List.of();

        Page<MyTeamResponseDto> pageResult = new PageImpl<>(pageContent, pageable, posts.size());

        return PagedResponse.of(pageResult);
    }
}
