package teamficial.teamficial_be.domain.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import teamficial.teamficial_be.domain.application.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, Integer> {

    @Query(value = "SELECT EXISTS(SELECT 1 FROM application WHERE user_id = :userId AND recruiting_post_id = :recruitingPostId)", nativeQuery = true)
    int existsByUserIdAndRecruitingPostId(Long userId, Long recruitingPostId);

}
