package teamficial.teamficial_be.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamficial.teamficial_be.domain.application.dto.response.ApplicationResponseDto;
import teamficial.teamficial_be.domain.application.entity.Application;
import teamficial.teamficial_be.domain.application.entity.ApplicationStatus;
import teamficial.teamficial_be.domain.application.service.ApplicationService;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;
import teamficial.teamficial_be.domain.recruitingPost.service.RecruitingPostService;
import teamficial.teamficial_be.domain.user.dto.CurrentApplicationResponseDto;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;
import teamficial.teamficial_be.global.enums.Position;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MypageService {

    private final RecruitingPostService recruitingPostService;
    private final ApplicationService applicationService;

    @Transactional(readOnly = true)
    public CurrentApplicationResponseDto getCurrentApplication(Long recruitingPostId, User user, Position position) {

        RecruitingPost recruitingPost = recruitingPostService.getRecruitingPostById(recruitingPostId);
        recruitingPostService.validatePostOwner(user,recruitingPost);

        List<Application> applications = applicationService.getApplications(recruitingPost);

        if (position != null) {
            applications = applications.stream()
                    .filter(application -> application.getProfile().getPosition() == position)
                    .toList();
        }

        return CurrentApplicationResponseDto.from(recruitingPost, applications);
    }

    @Transactional
    public void closedApplication(User user, Long recruitingPostId) {
        RecruitingPost recruitingPost = recruitingPostService.getRecruitingPostById(recruitingPostId);

        recruitingPostService.validatePostOwner(user, recruitingPost);

        recruitingPost.closedRecruitingPost();
        List<Application> applications = applicationService.getApplications(recruitingPost);
        applications.stream()
                .filter(app -> app.getApplicationStatus() == ApplicationStatus.OPEN)
                .forEach(app -> app.updateStatus(ApplicationStatus.CLOSED));

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
    public void confirmedApplicant(User user, Long recruitingPostId, Long applicationId) {
        RecruitingPost recruitingPost = recruitingPostService.getRecruitingPostById(recruitingPostId);
        recruitingPostService.validatePostOwner(user, recruitingPost);

        Application application = applicationService.getApplication(applicationId);
        if (!application.getRecruitingPost().getId().equals(recruitingPostId)) {
            throw new GeneralException(ErrorStatus._FORBIDDEN);
        }

        application.updateStatus(ApplicationStatus.CONFIRMED);
        applicationService.saveApplication(application);
    }
}
