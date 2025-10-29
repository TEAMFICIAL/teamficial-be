package teamficial.teamficial_be.domain.recruitingPost.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;

import java.util.Optional;

@Repository
public interface RecruitingPostRepository extends JpaRepository<RecruitingPost, Long>, RecruitingPostRepositoryCustom  {
    @Query("SELECT rp FROM RecruitingPost rp " +
            "JOIN FETCH rp.profile " +
            "WHERE rp.id = :id")
    Optional<RecruitingPost> findByIdWithProfile(@Param("id") Long id);

    Page<RecruitingPost> findAllByProfile_User_Id(Long userId, Pageable pageable);
}
