package teamficial.teamficial_be.domain.keyword.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import teamficial.teamficial_be.domain.keyword.entity.HeadKeyword;
import teamficial.teamficial_be.domain.profile.entity.Profile;

import java.util.List;

public interface HeadKeywordRepository extends JpaRepository<HeadKeyword, Long> {
    @Query("SELECT hk FROM HeadKeyword hk JOIN FETCH hk.profile WHERE hk.profile = :profile")
    List<HeadKeyword> findAllByProfile(Profile profile);

    int countByProfile(Profile profile);


    HeadKeyword findByProfileAndId(Profile profile, Long oldHeadKeywordId);
}
