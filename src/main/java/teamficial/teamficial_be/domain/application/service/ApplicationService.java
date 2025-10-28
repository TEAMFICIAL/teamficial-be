package teamficial.teamficial_be.domain.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamficial.teamficial_be.domain.application.entity.Application;
import teamficial.teamficial_be.domain.application.repository.ApplicationRepository;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.handler.NotFoundHandler;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    public Application getApplication(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(()-> new NotFoundHandler(ErrorStatus.NOT_FOUND_APPLICAION));
    }

    public List<Application> getApplications(RecruitingPost recruitingPost) {
        return applicationRepository.findAllByRecruitingPost(recruitingPost);
    }

    public void saveApplication(Application application) {
        applicationRepository.save(application);
    }

    public void saveApplications(List<Application> applications) {
        applicationRepository.saveAll(applications);
    }
}
