package teamficial.teamficial_be.domain.confirmed.entity;

import jakarta.persistence.*;
import lombok.*;
import teamficial.teamficial_be.domain.profile.entity.WorkingTime;
import teamficial.teamficial_be.global.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmedProfile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "confirmed_profile_id")
    private Long id;

    @OneToMany(mappedBy = "confirmedProfile",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<ConfirmedProfileLink> confirmedProfileLinks= new ArrayList<>();

    @OneToMany(mappedBy = "confirmedProfile",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<ConfirmedHeadKeyword> confirmedHeadKeywords = new ArrayList<>();

    @Column(name = "user_name", length = 50, nullable = false)
    private String userName;

    @Column(name = "profile_name", length = 50)
    private String profileName;

    @Column(name = "profile_image", length = 255)
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "working_time")
    private WorkingTime workingTime;

    @Column(name = "contact_way", length = 255)
    private String contactWay;

    public void addProfileLink(ConfirmedProfileLink profileLink) {
        confirmedProfileLinks.add(profileLink);
        profileLink.setConfirmedProfile(this);
    }

    public void addHeadKeyword(ConfirmedHeadKeyword keyword) {
        confirmedHeadKeywords.add(keyword);
        keyword.setConfirmedProfile(this);
    }
}
