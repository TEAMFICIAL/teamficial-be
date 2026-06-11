package teamficial.teamficial_be.domain.recruitingPost.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamficial.teamficial_be.domain.recruitingPost.dto.RecruitingPostDto;
import teamficial.teamficial_be.domain.recruitingPost.dto.RecruitingPostPagingDto;
import teamficial.teamficial_be.domain.recruitingPost.entity.ProgressWay;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingStatus;
import teamficial.teamficial_be.global.enums.Position;

import java.util.List;

public interface RecruitingPostRepositoryCustom {
    Page<RecruitingPostPagingDto.RecruitingPostFlatDto> findByFilters(
            RecruitingStatus status,
            Position position,
            ProgressWay progressWay,
            Pageable pageable);


    List<RecruitingPostPagingDto.RecruitingDetailFlatDto> findPositionsByPostIds(List<Long> postIds);
}
