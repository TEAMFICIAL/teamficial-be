package teamficial.teamficial_be.domain.application.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import teamficial.teamficial_be.domain.application.entity.Application;
import teamficial.teamficial_be.domain.application.entity.ApplicationStatus;
import teamficial.teamficial_be.domain.profile.entity.Profile;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;
import teamficial.teamficial_be.domain.user.entity.User;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Query(value = "SELECT EXISTS(SELECT 1 FROM application WHERE user_id = :userId AND recruiting_post_id = :recruitingPostId)", nativeQuery = true)
    int existsByUserIdAndRecruitingPostId(Long userId, Long recruitingPostId);

    List<Application> findAllByRecruitingPost(RecruitingPost recruitingPost);

    @EntityGraph(attributePaths = {"user", "profile"})
    Page<Application> findAllByUser(User user, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "profile"})
    Page<Application> findAllByUserAndApplicationStatus(User user, Pageable pageable, ApplicationStatus status);

    @Query("SELECT ap FROM Application ap " +
            "JOIN FETCH ap.user " +
            "LEFT JOIN FETCH ap.profile " +
            "WHERE ap.user = :user " +
            "ORDER BY ap.createdAt DESC " +
            "LIMIT 3")
    List<Application> findTop3ByUserOrderByCreatedAtDesc(User user);

    int countAllByRecruitingPost(RecruitingPost recruitingPost);

    @Query("SELECT a.recruitingPost.id, COUNT(a) " +
            "FROM Application a " +
            "WHERE a.recruitingPost.id IN :postIds " +
            "GROUP BY a.recruitingPost.id")
    List<Object[]> countByRecruitingPostIds(@Param("postIds") List<Long> postIds);

    @Query("SELECT ap FROM Application ap " +
            "JOIN FETCH ap.profile p " +
            "WHERE p = :profile")
    List<Application> findAllByProfile(Profile profile);

    Page<Application> findAllByUserAndApplicationStatusIn(User user, Pageable pageable, List<ApplicationStatus> status);
}
