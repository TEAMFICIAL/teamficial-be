package teamficial.teamficial_be.domain.profile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamficial.teamficial_be.domain.profile.dto.request.ProfileRequestDto;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.global.entity.BaseEntity;
import teamficial.teamficial_be.global.enums.Position;

@Entity
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
    @Column(name = "position", nullable = false)
    private Position position;

    @Enumerated(EnumType.STRING)
    @Column(name = "working_time", nullable = false)
    private WorkingTime workingTime;

    @Column(name = "link", length = 255)
    private String link;

    @Column(name = "contact_way", length = 255)
    private String contactWay;

    public void update(ProfileRequestDto dto) {
        this.profileName = dto.getProfileName();
        this.position = dto.getPosition();
        this.workingTime = dto.getWorkingTime();
        this.link = dto.getLink();
        this.contactWay = dto.getContactWay();
    }

    public void updateProfileImage(String newProfileImage){
        this.profileImage = newProfileImage;
    }

    public void deleteProfileImage(){
        this.profileImage = null;
    }
}
