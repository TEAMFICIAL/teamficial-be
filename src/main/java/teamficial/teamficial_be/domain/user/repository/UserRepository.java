package teamficial.teamficial_be.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamficial.teamficial_be.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
