package teamficial.teamficial_be.domain.recruitingPost.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamficial.teamficial_be.domain.profile.entity.Profile;
import teamficial.teamficial_be.global.entity.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recruiting_post")
public class RecruitingPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruiting_post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Enumerated(EnumType.STRING)
    @Column(name = "progress_way", nullable = false)
    private ProgressWay progressWay;

    @Column(name = "contact_way", length = 255)
    private String contactWay;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "period", nullable = false)
    private Period period;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RecruitingStatus status;

    /** 프로젝트 설명 */
    @Lob
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    /** 공고 제목 */
    @Column(name = "title", length = 50, nullable = false)
    private String title;
}
