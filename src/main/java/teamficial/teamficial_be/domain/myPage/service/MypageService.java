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
import teamficial.teamficial_be.domain.myPage.dto.response.MyApplicationResponseDto;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;
import teamficial.teamficial_be.domain.recruitingPost.service.RecruitingPostService;
import teamficial.teamficial_be.domain.myPage.dto.response.CurrentApplicationDetailResponseDto;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;
import teamficial.teamficial_be.global.enums.Position;
import teamficial.teamficial_be.global.util.PagedResponse;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MypageService {

    private final RecruitingPostService recruitingPostService;
    private final ApplicationService applicationService;

    @Transactional(readOnly = true)
    public PagedResponse<MyApplicationResponseDto> getAllApplications(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Application> applicationPage = applicationService.getApplicationsByUser(user,pageable);

        Page<MyApplicationResponseDto> dtoPage = applicationPage
                .map(application -> MyApplicationResponseDto.of(application.getRecruitingPost()));

        return PagedResponse.of(dtoPage);
    }



    @Transactional(readOnly = true)
    public CurrentApplicationDetailResponseDto getCurrentApplication(Long recruitingPostId, User user, Position position) {

        RecruitingPost recruitingPost = recruitingPostService.getRecruitingPostById(recruitingPostId);
        recruitingPostService.validatePostOwner(user,recruitingPost);

        List<Application> applications = applicationService.getApplicationsByRecruitingPost(recruitingPost);

        if (position != null) {
            applications = applications.stream()
                    .filter(application -> application.getProfile().getPosition() == position)
                    .toList();
        }

        return CurrentApplicationDetailResponseDto.from(recruitingPost, applications);
    }

    @Transactional
    public void closedApplication(User user, Long recruitingPostId) {
        RecruitingPost recruitingPost = recruitingPostService.getRecruitingPostById(recruitingPostId);

        recruitingPostService.validatePostOwner(user, recruitingPost);

        recruitingPost.closedRecruitingPost();
        List<Application> applications = applicationService.getApplicationsByRecruitingPost(recruitingPost);
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
