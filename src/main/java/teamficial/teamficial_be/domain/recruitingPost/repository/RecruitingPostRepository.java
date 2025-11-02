package teamficial.teamficial_be.domain.recruitingPost.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingStatus;
import teamficial.teamficial_be.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecruitingPostRepository extends JpaRepository<RecruitingPost, Long>, RecruitingPostRepositoryCustom  {
    @Query("SELECT rp FROM RecruitingPost rp " +
            "JOIN FETCH rp.profile " +
            "WHERE rp.id = :id")
    Optional<RecruitingPost> findByIdWithProfile(@Param("id") Long id);

    @Query("SELECT rp FROM RecruitingPost rp " +
            "LEFT JOIN FETCH rp.applications " +
            "WHERE rp.profile.user.id = :userId")
    Page<RecruitingPost> findAllByProfile_User_Id(Long userId, Pageable pageable);

    @Query("SELECT rp FROM RecruitingPost rp " +
            "LEFT JOIN FETCH rp.applications " +
            "WHERE rp.profile.user.id = :userId AND rp.status = :recruitingStatus")
    Page<RecruitingPost> findAllByProfile_User_IdAndStatus(Long userId, Pageable pageable, RecruitingStatus recruitingStatus);

    List<RecruitingPost> findTop3ByUserOrderByDeadlineAsc(User user);
}
