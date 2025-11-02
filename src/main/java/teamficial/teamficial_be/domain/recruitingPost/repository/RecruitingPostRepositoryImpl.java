package teamficial.teamficial_be.domain.recruitingPost.repository;

import com.amazonaws.event.request.Progress;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
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

import java.util.List;

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

        Long total = queryFactory
                .select(Wildcard.count)
                .from(post)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }
}
