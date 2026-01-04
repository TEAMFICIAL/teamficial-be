package teamficial.teamficial_be.domain.recruitingPost.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import teamficial.teamficial_be.domain.application.entity.Application;
import teamficial.teamficial_be.domain.profile.entity.Profile;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingStatus;
import teamficial.teamficial_be.domain.user.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecruitingPostRepository extends JpaRepository<RecruitingPost, Long>, RecruitingPostRepositoryCustom  {
    @Query("SELECT rp FROM RecruitingPost rp " +
            "JOIN FETCH rp.profile " +
            "WHERE rp.id = :id")
    Optional<RecruitingPost> findByIdWithProfile(@Param("id") Long id);

    @EntityGraph(attributePaths = {"user", "profile"})
    Page<RecruitingPost> findAllByUser(User user, Pageable pageable);

    @Query("SELECT rp FROM RecruitingPost rp " +
            "JOIN FETCH rp.user " +
            "WHERE rp.user = :user " +
            "ORDER BY rp.deadline ASC ")
    List<RecruitingPost> findAllByUser(User user);

    @EntityGraph(attributePaths = {"user", "profile"})
    Page<RecruitingPost> findAllByUserAndStatus(User user, Pageable pageable, RecruitingStatus recruitingStatus);

    @Modifying
    @Query("UPDATE RecruitingPost p SET p.status = 'CLOSED' WHERE p.deadline < :today AND p.status <> 'CLOSED'")
    int updateStatusToClosed(@Param("today") LocalDate today);

    @Query("SELECT rp FROM RecruitingPost rp " +
            "JOIN FETCH rp.profile p " +
            "WHERE p = :profile")
    List<RecruitingPost> findAllByProfile(Profile profile);

    @Query("SELECT rp FROM RecruitingPost rp " +
            "JOIN FETCH rp.user " +
            "LEFT JOIN FETCH rp.profile " +
            "WHERE rp.user = :user " +
            "ORDER BY rp.deadline ASC " +
            "LIMIT 3")
    List<RecruitingPost> findTop3ByUserOrderByDeadlineAsc(User user);

    @Query("SELECT rp FROM RecruitingPost rp " +
            "JOIN FETCH rp.user " +
            "WHERE rp.user = :user AND rp.status = :recruitingStatus")
    List<RecruitingPost> findAllByUserAndStatus(User user, RecruitingStatus recruitingStatus);

    @Query("""
    select rp
    from RecruitingPost rp
    left join fetch rp.recruitingDetails
    where rp.id = :id
""")
    Optional<RecruitingPost> findWithDetailsById(@Param("id") Long id);


}
