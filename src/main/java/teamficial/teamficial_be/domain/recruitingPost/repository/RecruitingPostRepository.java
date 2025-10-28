package teamficial.teamficial_be.domain.recruitingPost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;

public interface RecruitingPostRepository extends JpaRepository<RecruitingPost, Long> {
}
