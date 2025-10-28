package teamficial.teamficial_be.domain.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamficial.teamficial_be.domain.application.entity.Application;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findAllByRecruitingPost(RecruitingPost recruitingPost);
}
