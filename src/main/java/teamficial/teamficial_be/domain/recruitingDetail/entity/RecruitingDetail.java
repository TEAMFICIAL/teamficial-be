package teamficial.teamficial_be.domain.recruitingDetail.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;
import teamficial.teamficial_be.global.entity.BaseEntity;
import teamficial.teamficial_be.global.enums.Position;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recruiting_detail")
public class RecruitingDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruiting_detail_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "position", nullable = false)
    private Position position;

    @Column(name = "count", nullable = false)
    private Integer count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiting_post_id", nullable = false)
    private RecruitingPost recruitingPost;

    public String getRecruitingDetail() {
        String positionString = position.getDescription();
        String countString = count.toString();
        return positionString + " " + countString + "명";
    }
}
