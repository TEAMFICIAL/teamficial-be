package teamficial.teamficial_be.global.security.repository;

import org.springframework.data.repository.CrudRepository;
import teamficial.teamficial_be.global.security.entity.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
