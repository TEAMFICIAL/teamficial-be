package teamficial.teamficial_be.domain.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamficial.teamficial_be.domain.application.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    boolean existsByUserIdAndRecruitingPostId(Long userId, Long recruitingPostId);

}
