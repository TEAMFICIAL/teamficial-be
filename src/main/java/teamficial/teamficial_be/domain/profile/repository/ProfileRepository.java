package teamficial.teamficial_be.domain.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamficial.teamficial_be.domain.profile.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
