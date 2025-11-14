package teamficial.teamficial_be.domain.keyword.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import teamficial.teamficial_be.domain.keyword.entity.Keyword;
import teamficial.teamficial_be.domain.user.entity.User;

import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    @EntityGraph(attributePaths = {"user"})
    Page<Keyword> findAllByUser(User user, Pageable pageable);

    Optional<Keyword> findByUserAndKeywordName(User user, String bestKeyword);
}
