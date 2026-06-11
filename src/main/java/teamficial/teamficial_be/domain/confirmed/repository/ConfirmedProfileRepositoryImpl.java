package teamficial.teamficial_be.domain.confirmed.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import teamficial.teamficial_be.domain.confirmed.entity.ConfirmedProfile;
import teamficial.teamficial_be.domain.confirmed.entity.QConfirmedHeadKeyword;
import teamficial.teamficial_be.domain.confirmed.entity.QConfirmedProfile;
import teamficial.teamficial_be.domain.confirmed.entity.QConfirmedProfileLink;
import teamficial.teamficial_be.global.enums.Position;

import java.util.List;

@RequiredArgsConstructor
public class ConfirmedProfileRepositoryImpl implements ConfirmedProfileRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QConfirmedProfileLink link = QConfirmedProfileLink.confirmedProfileLink;
    QConfirmedHeadKeyword keyword = QConfirmedHeadKeyword.confirmedHeadKeyword;
    QConfirmedProfile cp = QConfirmedProfile.confirmedProfile;

    @Override
    public List<ConfirmedProfile> findByPostIdAndPosition(Long postId, Position position) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(cp.recruitingPost.id.eq(postId));

        if (position != null) {
            builder.and(cp.position.eq(position));
        }

        return queryFactory
                .selectDistinct(cp)
                .from(cp)
                .leftJoin(cp.confirmedProfileLinks, link).fetchJoin()
                .where(builder)
                .orderBy(cp.id.desc())
                .fetch();
        }
}
