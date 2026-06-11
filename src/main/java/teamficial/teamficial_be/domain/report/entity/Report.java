package teamficial.teamficial_be.domain.report.entity;

import jakarta.persistence.*;
import lombok.*;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.global.entity.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "reported_comment_id"})
})
public class Report extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType reportType;

    @Column(length = 500, nullable = false)
    private String content;

    private boolean isApplied;

    @Column(length = 100)
    private String reportEtc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "reported_comment_id", nullable = false)
    private Long reportedCommentId;

    public void reportAccept(){
        this.isApplied = true;
    }
}
