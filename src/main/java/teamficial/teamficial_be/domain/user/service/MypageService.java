package teamficial.teamficial_be.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamficial.teamficial_be.domain.application.entity.Application;
import teamficial.teamficial_be.domain.application.service.ApplicationService;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;
import teamficial.teamficial_be.domain.recruitingPost.service.RecruitingPostService;
import teamficial.teamficial_be.domain.user.dto.CurrentApplicationResponseDto;
import teamficial.teamficial_be.domain.user.entity.User;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MypageService {

    private final RecruitingPostService recruitingPostService;
    private final ApplicationService applicationService;

    @Transactional(readOnly = true)
    public CurrentApplicationResponseDto getCurrentApplication(Long recruitingPostId, User user) {

        RecruitingPost recruitingPost = recruitingPostService.getRecruitingPostById(recruitingPostId);
        recruitingPostService.validatePostOwner(user,recruitingPost);

        List<Application> applications = applicationService.getApplications(recruitingPost);

        return CurrentApplicationResponseDto.from(recruitingPost, applications);
    }
}
