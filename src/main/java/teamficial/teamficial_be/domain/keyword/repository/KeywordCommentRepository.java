package teamficial.teamficial_be.domain.keyword.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import teamficial.teamficial_be.domain.keyword.entity.Keyword;
import teamficial.teamficial_be.domain.keyword.entity.KeywordComment;

public interface KeywordCommentRepository extends JpaRepository<KeywordComment, Long> {

    Slice<KeywordComment> findAllByKeyword(Keyword keyword, Pageable pageable);


}
