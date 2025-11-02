package teamficial.teamficial_be.domain.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import teamficial.teamficial_be.domain.profile.entity.Profile;
import teamficial.teamficial_be.domain.user.entity.User;

import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    @Query("SELECT p FROM Profile p " +
            "WHERE p.user = :user")
    List<Profile> findAllByUser(@Param("user") User user);
}
