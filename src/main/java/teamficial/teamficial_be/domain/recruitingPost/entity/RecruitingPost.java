package teamficial.teamficial_be.domain.recruitingPost.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamficial.teamficial_be.domain.application.entity.Application;
import teamficial.teamficial_be.domain.profile.entity.Profile;
import teamficial.teamficial_be.domain.recruitingDetail.entity.RecruitingDetail;
import teamficial.teamficial_be.domain.recruitingPost.dto.RecruitingPostDTO;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.global.entity.BaseEntity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "recruitingPost", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Application> applications = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "progress_way", nullable = false)
    private ProgressWay progressWay;

    @Column(name = "contact_way", length = 255)
    private String contactWay;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "period", nullable = false)
    private Period period;

    @Column(name = "deadline")
    private LocalDate deadline;

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

    @OneToMany(mappedBy = "recruitingPost", cascade = CascadeType.REMOVE,orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RecruitingDetail> recruitingDetails = new ArrayList<>();

    public void closedRecruitingPost(){
        this.status = RecruitingStatus.CLOSED;
    }

    public int getTotalApplicants(){
        return this.applications.size();
    }

    public void update(RecruitingPostDTO.RecruitingPostModifyRequestDTO dto) {
        if (dto.getProgressWay() != null) this.progressWay = dto.getProgressWay();
        if (dto.getContactWay() != null) this.contactWay = dto.getContactWay();
        if (dto.getStartDate() != null) this.startDate = dto.getStartDate();
        if (dto.getPeriod() != null) this.period = dto.getPeriod();
        if (dto.getDeadline() != null) this.deadline = dto.getDeadline();
        if (dto.getStatus() != null) this.status = dto.getStatus();
        if (dto.getContent() != null) this.content = dto.getContent();
        if (dto.getTitle() != null) this.title = dto.getTitle();
    }

    public long getDDay(){
        if (this.deadline == null) return -1L;

        if (this.deadline.isBefore(LocalDate.now())) {
            this.status = RecruitingStatus.CLOSED;
            return 0L;
        }

        return ChronoUnit.DAYS.between(this.deadline, LocalDate.now());
    }
}
