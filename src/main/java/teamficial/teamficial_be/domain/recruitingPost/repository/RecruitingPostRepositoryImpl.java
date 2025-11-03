package teamficial.teamficial_be.domain.recruitingPost.repository;

import com.amazonaws.event.request.Progress;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.collection.spi.PersistentBag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import teamficial.teamficial_be.domain.recruitingDetail.entity.QRecruitingDetail;
import teamficial.teamficial_be.domain.recruitingPost.dto.QRecruitingPostDTO_RecruitingPostsResponseDTO;
import teamficial.teamficial_be.domain.recruitingPost.dto.RecruitingPostDTO;
import teamficial.teamficial_be.domain.recruitingPost.entity.ProgressWay;
import teamficial.teamficial_be.domain.recruitingPost.entity.QRecruitingPost;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingStatus;
import teamficial.teamficial_be.global.enums.Position;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RecruitingPostRepositoryImpl implements RecruitingPostRepositoryCustom {
    private final JPAQueryFactory queryFactory;


    @Override
    public Page<RecruitingPostDTO.RecruitingPostsResponseDTO> findByFilters(
            RecruitingStatus status,
            Position position,
            ProgressWay progressWay,
            Pageable pageable) {

        QRecruitingPost post = QRecruitingPost.recruitingPost;
        QRecruitingDetail postDetail = QRecruitingDetail.recruitingDetail;
        BooleanBuilder builder = new BooleanBuilder();

        if (status != null) builder.and(post.status.eq(status));
        if (progressWay != null) builder.and(post.progressWay.eq(progressWay));
        if (position != null) builder.and(postDetail.position.eq(position));

        List<RecruitingPostDTO.RecruitingPostsResponseDTO> content = queryFactory
                .select(new QRecruitingPostDTO_RecruitingPostsResponseDTO(
                        post.id,
                        post.profile.id,
                        post.profile.userName,
                        post.profile.profileImage,
                        post.progressWay,
                        post.contactWay,
                        post.startDate,
                        post.period,
                        post.deadline,
                        post.status,
                        post.content,
                        post.title,
                        post.createdAt
                ))
                .from(post)
                .distinct()
                .leftJoin(post.recruitingDetails, postDetail)
                .where(builder)
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Long> postIds = content.stream()
                        .map(RecruitingPostDTO.RecruitingPostsResponseDTO::getPostId)
                        .toList();

        if (postIds.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        Map<Long, List<RecruitingPostDTO.RecruitingPositionDTO>> positionMap = queryFactory
                .select(Projections.constructor(RecruitingPostDTO.RecruitingPositionQueryDTO.class,
                        postDetail.recruitingPost.id,
                        postDetail.position,
                        postDetail.count))
                .from(postDetail)
                .where(postDetail.recruitingPost.id.in(postIds))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(RecruitingPostDTO.RecruitingPositionQueryDTO::getPostId))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(p -> RecruitingPostDTO.RecruitingPositionDTO.builder()
                                        .position(p.getPosition())
                                        .count(p.getCount())
                                        .build())
                                .toList()
                ));

        content.forEach(dto ->
                dto.setRecruitingPositions(
                        positionMap.getOrDefault(dto.getPostId(), Collections.emptyList())
                )
        );


        Long total = queryFactory
                .select(Wildcard.count)
                .from(post)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }
}
