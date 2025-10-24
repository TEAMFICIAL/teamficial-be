package teamficial.teamficial_be.domain.recruitingDetail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamficial.teamficial_be.domain.recruitingDetail.entity.RecruitingDetail;

import java.util.List;

@Repository
public interface RecruitingDetailRepository extends JpaRepository<RecruitingDetail, Long> {
    List<RecruitingDetail> findByRecruitingPostId(Long postId);
}
