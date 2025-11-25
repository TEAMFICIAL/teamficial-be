package teamficial.teamficial_be.domain.keyword.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamficial.teamficial_be.domain.keyword.entity.KeywordCommentSession;

public interface KeywordCommentSessionRepository extends JpaRepository<KeywordCommentSession, Long> {

    boolean existsByOwnerIdAndWriterId(Long ownerId, Long writerId);
}
