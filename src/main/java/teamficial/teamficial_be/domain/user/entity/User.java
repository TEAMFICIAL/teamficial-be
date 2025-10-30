package teamficial.teamficial_be.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import teamficial.teamficial_be.domain.profile.entity.Profile;
import teamficial.teamficial_be.global.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @UuidGenerator
    @Column(nullable = false, unique = true, updatable=false, length=36)
    private String uuid;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false,length = 20)
    private String name;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type")
    private LoginType loginType;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Profile> profiles = new ArrayList<>();

    @PrePersist
    public void prePersistUuid() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        }
    }
}
