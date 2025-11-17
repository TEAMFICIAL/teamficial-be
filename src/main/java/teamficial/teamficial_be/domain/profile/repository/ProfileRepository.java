package teamficial.teamficial_be.domain.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import teamficial.teamficial_be.domain.profile.entity.Profile;
import teamficial.teamficial_be.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    List<Profile> findAllByUserAndIsDeletedFalse(User user);

    @Query("select count(p) from Profile  p where p.user = :user and p.isDeleted = false")
    int countByUser(User user);

    @Query("SELECT p FROM Profile p " +
            "LEFT JOIN FETCH p.profileLinks " +
            "WHERE p.id = :id")
    Optional<Profile> findWithLinksById(@Param("id") Long id);
}
