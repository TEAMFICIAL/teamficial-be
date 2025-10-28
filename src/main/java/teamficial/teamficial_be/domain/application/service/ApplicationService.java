package teamficial.teamficial_be.domain.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamficial.teamficial_be.domain.application.entity.Application;
import teamficial.teamficial_be.domain.application.repository.ApplicationRepository;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    public List<Application> getApplications(RecruitingPost recruitingPost) {
        return applicationRepository.findAllByRecruitingPost(recruitingPost);
    }

    public void saveApplications(List<Application> applications) {
        applicationRepository.saveAll(applications);
    }
}
