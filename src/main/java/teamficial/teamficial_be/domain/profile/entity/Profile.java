package teamficial.teamficial_be.domain.profile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import teamficial.teamficial_be.domain.keyword.entity.HeadKeyword;
import teamficial.teamficial_be.domain.profile.dto.request.ProfileRequestDto;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.global.entity.BaseEntity;
import teamficial.teamficial_be.global.enums.Position;

import java.util.ArrayList;
import java.util.List;

@Entity
@SQLDelete(sql = "UPDATE profile SET is_deleted = 1 WHERE profile_id = ?")
@Where(clause = "is_deleted = 0")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "profile")
public class Profile extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "user_name", length = 50, nullable = false)
    private String userName;

    @Column(name = "profile_name", length = 50, nullable = false)
    private String profileName;

    @Column(name = "profile_image", length = 255)
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "working_time", nullable = false)
    private WorkingTime workingTime;

    @OneToMany(mappedBy = "profile",cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<ProfileLink> profileLinks= new ArrayList<>();

    @OneToMany(mappedBy = "profile",cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<HeadKeyword> headKeywords= new ArrayList<>();

    @Column(name = "contact_way", length = 255)
    private String contactWay;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "tinyint(1) default 0")
    private boolean isDeleted = false;

    public void update(ProfileRequestDto dto) {
        this.profileName = dto.getProfileName();
        this.workingTime = dto.getWorkingTime();
        this.contactWay = dto.getContactWay();
    }

    public void updateProfileImage(String newProfileImage){
        this.profileImage = newProfileImage;
    }

    public void deleteProfileImage(){
        this.profileImage = null;
    }
    public void addLink(String link) {
        ProfileLink newProfileLink = ProfileLink.builder()
                .profile(this)
                .link(link)
                .build();
        profileLinks.add(newProfileLink);
    }

    public void clearLinks() {
        this.profileLinks.clear();
    }

}
