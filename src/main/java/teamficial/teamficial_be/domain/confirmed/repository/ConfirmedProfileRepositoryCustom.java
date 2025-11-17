package teamficial.teamficial_be.domain.confirmed.repository;

import teamficial.teamficial_be.domain.confirmed.entity.ConfirmedProfile;
import teamficial.teamficial_be.global.enums.Position;

import java.util.List;

public interface ConfirmedProfileRepositoryCustom {
    List<ConfirmedProfile> findByPostIdAndPosition(Long postId, Position position);
}
