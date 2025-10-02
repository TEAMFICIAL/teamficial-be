package teamficial.teamficial_be.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import teamficial.teamficial_be.global.entity.BaseEntity;


@Entity
@Getter
@Builder
public class User extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String userName;

    private String password;

    private UserRole userRole;


}
