package teamficial.teamficial_be.domain.confirmed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamficial.teamficial_be.domain.confirmed.entity.ConfirmedProfile;

@Repository
public interface ConfirmedProfileRepository extends JpaRepository<ConfirmedProfile, Long>, ConfirmedProfileRepositoryCustom {
}
