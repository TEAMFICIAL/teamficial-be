package teamficial.teamficial_be.domain.keyword.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import teamficial.teamficial_be.domain.keyword.entity.Keyword;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
}
