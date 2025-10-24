package teamficial.teamficial_be.domain.recruitingPost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;

@Repository
public interface RecruitingPostRepository extends JpaRepository<RecruitingPost, Long> {
}
