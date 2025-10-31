package teamficial.teamficial_be.domain.application.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import teamficial.teamficial_be.domain.application.entity.Application;
import teamficial.teamficial_be.domain.application.entity.ApplicationStatus;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;
import teamficial.teamficial_be.domain.user.entity.User;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Query(value = "SELECT EXISTS(SELECT 1 FROM application WHERE user_id = :userId AND recruiting_post_id = :recruitingPostId)", nativeQuery = true)
    int existsByUserIdAndRecruitingPostId(Long userId, Long recruitingPostId);

    List<Application> findAllByRecruitingPost(RecruitingPost recruitingPost);

    @EntityGraph(attributePaths = {"user"})
    Page<Application> findAllByUser(User user, Pageable pageable);

    @EntityGraph(attributePaths = {"user"})
    Page<Application> findAllByUserAndApplicationStatus(User user, Pageable pageable, ApplicationStatus status);
}
