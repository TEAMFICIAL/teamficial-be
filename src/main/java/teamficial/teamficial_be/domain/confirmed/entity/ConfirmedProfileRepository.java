package teamficial.teamficial_be.domain.confirmed.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmedProfileRepository extends JpaRepository<ConfirmedProfile, Long> {
}
