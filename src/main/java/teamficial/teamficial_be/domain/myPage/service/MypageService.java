package teamficial.teamficial_be.domain.myPage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamficial.teamficial_be.domain.application.dto.response.ApplicationResponseDto;
import teamficial.teamficial_be.domain.application.entity.Application;
import teamficial.teamficial_be.domain.application.entity.ApplicationStatus;
import teamficial.teamficial_be.domain.application.service.ApplicationService;
import teamficial.teamficial_be.domain.myPage.dto.response.CurrentApplicantResponseDto;
import teamficial.teamficial_be.domain.myPage.dto.response.DashboardResponseDto;
import teamficial.teamficial_be.domain.myPage.dto.response.MyApplicationResponseDto;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingStatus;
import teamficial.teamficial_be.domain.recruitingPost.service.RecruitingPostService;
import teamficial.teamficial_be.domain.myPage.dto.response.CurrentApplicationDetailResponseDto;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;
import teamficial.teamficial_be.global.enums.Position;
import teamficial.teamficial_be.global.redis.RedisService;
import teamficial.teamficial_be.global.util.PagedResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MypageService {

    private final RecruitingPostService recruitingPostService;
    private final ApplicationService applicationService;
    private final RedisService redisService;

    @Transactional(readOnly = true)
    public PagedResponse<MyApplicationResponseDto> getAllApplications(User user, int page, int size,ApplicationStatus applicationStatus) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "recruitingPost.createdAt"));

        Page<Application> applicationPage = applicationService.getApplicationsByUserAndStatus(user,pageable,applicationStatus);

        Page<MyApplicationResponseDto> dtoPage = applicationPage
                .map(application -> {
                    RecruitingPost recruitingPost = application.getRecruitingPost();
                    ApplicationStatus status = application.getApplicationStatus();

                    if (recruitingPost.getStatus() == RecruitingStatus.OPEN || status ==ApplicationStatus.TEMP_SAVED) {
                        return MyApplicationResponseDto.of(application.getRecruitingPost(), ApplicationStatus.MATCHING.getDescription());
                    }

                    return MyApplicationResponseDto.of(application.getRecruitingPost(), ApplicationStatus.MATCHING.getDescription());
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

        List<String> cachedValues = redisService.getValues(keys);

        Map<Long,Integer> resultMap = new HashMap<>();
        List<Long> missIds = new ArrayList<>();

        for (int i=0; i < postIds.size(); i++) {
            Long postId = postIds.get(i);
            String key = keys.get(i);
            String value = cachedValues.get(i);

            if (value != null && !value.isEmpty()) {
                resultMap.put(postId, Integer.parseInt(value));
                log.debug("캐시 히트 key={}, value={}, postId={}", key, value, postId);
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


        return CurrentApplicationDetailResponseDto.from(recruitingPost, applications,dDay);
    }

    @Transactional
    public void closedApplication(User user, Long recruitingPostId) {
        RecruitingPost recruitingPost = recruitingPostService.getRecruitingPostById(recruitingPostId);

        recruitingPostService.validatePostOwner(user, recruitingPost);

        recruitingPost.closedRecruitingPost();
        List<Application> applications = applicationService.getApplicationsByRecruitingPost(recruitingPost);
        applications.stream()
                .filter(app -> app.getApplicationStatus() == ApplicationStatus.MATCHING)
                .forEach(app -> app.updateStatus(ApplicationStatus.MATCH_FAILED));

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
        List<RecruitingPost> recruitingPostList = recruitingPostService.getTop3ByUser(user);
        List<Application> applicationList = applicationService.getTop3ByUser(user);

        List<Long> postIds = recruitingPostList.stream()
                .map(RecruitingPost::getId)
                .toList();

        Map<Long,Integer> applicantCountMap = getApplicantCountsBatch(postIds);

        List<CurrentApplicantResponseDto> recruitingDtos = recruitingPostList.stream()
                .map(recruitingPost -> {
                    long dDay = recruitingPost.getDDay();
                    int totalApplicants = applicantCountMap.get(recruitingPost.getId());
                    return CurrentApplicantResponseDto.of(recruitingPost, dDay, totalApplicants);
                })
                .toList();

        List<MyApplicationResponseDto> applicationDtos = applicationList.stream()
                .map(app -> {
                    ApplicationStatus status = app.getApplicationStatus();
                    if (status == ApplicationStatus.TEMP_SAVED) {
                        return MyApplicationResponseDto.of(app.getRecruitingPost(), ApplicationStatus.TEMP_SAVED.getDescription());
                    }
                    return MyApplicationResponseDto.of(app.getRecruitingPost(), app.getApplicationStatus().getDescription());
                })
                .toList();

        return DashboardResponseDto.builder()
                .myRecruitingPost(recruitingDtos)
                .myApplications(applicationDtos)
                .build();
    }

}
