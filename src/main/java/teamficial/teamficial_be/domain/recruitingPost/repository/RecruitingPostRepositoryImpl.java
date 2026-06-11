package teamficial.teamficial_be.domain.recruitingPost.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import teamficial.teamficial_be.domain.recruitingDetail.entity.QRecruitingDetail;
import teamficial.teamficial_be.domain.recruitingPost.dto.RecruitingPostPagingDto;
import teamficial.teamficial_be.domain.recruitingPost.entity.ProgressWay;
import teamficial.teamficial_be.domain.recruitingPost.entity.QRecruitingPost;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingStatus;
import teamficial.teamficial_be.global.enums.Position;

import java.util.Collections;
import java.util.List;

import static teamficial.teamficial_be.domain.profile.entity.QProfile.profile;
import static teamficial.teamficial_be.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class RecruitingPostRepositoryImpl implements RecruitingPostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<RecruitingPostPagingDto.RecruitingPostFlatDto> findByFilters(
            RecruitingStatus status,
            Position position,
            ProgressWay progressWay,
            Pageable pageable) {

        QRecruitingPost post = QRecruitingPost.recruitingPost;
        QRecruitingDetail postDetail = QRecruitingDetail.recruitingDetail;
        BooleanBuilder builder = new BooleanBuilder();

        if (status != null) builder.and(post.status.eq(status));
        if (progressWay != null) builder.and(post.progressWay.eq(progressWay));

        List<Long> pagedPostIds;

        if (position == null) {
            pagedPostIds = queryFactory
                    .select(post.id)
                    .from(post)
                    .where(builder)
                    .orderBy(post.createdAt.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        } else {
            pagedPostIds = queryFactory
                    .select(post.id)
                    .from(post)
                    .join(post.recruitingDetails, postDetail)
                    .where(
                            builder.and(postDetail.position.eq(position))
                    )
                    .orderBy(post.createdAt.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        }

        if (pagedPostIds.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        List<RecruitingPostPagingDto.RecruitingPostFlatDto> flatPosts =
                queryFactory
                        .select(Projections.constructor(
                                RecruitingPostPagingDto.RecruitingPostFlatDto.class,
                                post.id,
                                post.profile.isDeleted,
                                post.profile.user.id,
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
                        .join(post.profile, profile)
                        .join(profile.user, user)
                        .where(post.id.in(pagedPostIds))
                        .orderBy(post.createdAt.desc())
                        .fetch();

        Long total;
        if (position == null) {
            total = queryFactory
                    .select(post.id.count())
                    .from(post)
                    .where(builder)
                    .fetchOne();
        } else {
            total = queryFactory
                    .select(post.id.countDistinct())
                    .from(post)
                    .join(post.recruitingDetails, postDetail)
                    .where(builder.and(postDetail.position.eq(position)))
                    .fetchOne();
        }

        return new PageImpl<>(flatPosts, pageable, total == null ? 0 : total);
    }

    @Override
    public List<RecruitingPostPagingDto.RecruitingDetailFlatDto> findPositionsByPostIds(List<Long> postIds) {
        QRecruitingDetail detail = QRecruitingDetail.recruitingDetail;

        return queryFactory
                .select(Projections.constructor(
                        RecruitingPostPagingDto.RecruitingDetailFlatDto.class,
                        detail.recruitingPost.id,
                        detail.position,
                        detail.count
                ))
                .from(detail)
                .where(detail.recruitingPost.id.in(postIds))
                .fetch();
    }
}
