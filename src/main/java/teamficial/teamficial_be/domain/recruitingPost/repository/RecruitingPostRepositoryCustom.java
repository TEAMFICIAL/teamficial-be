package teamficial.teamficial_be.domain.recruitingPost.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamficial.teamficial_be.domain.recruitingPost.dto.RecruitingPostDTO;
import teamficial.teamficial_be.domain.recruitingPost.entity.ProgressWay;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingStatus;
import teamficial.teamficial_be.global.enums.Position;

public interface RecruitingPostRepositoryCustom {
    Page<RecruitingPostDTO.RecruitingPostsResponseDTO> findByFilters(
            RecruitingStatus status,
            Position position,
            ProgressWay progressWay,
            Pageable pageable);
}
