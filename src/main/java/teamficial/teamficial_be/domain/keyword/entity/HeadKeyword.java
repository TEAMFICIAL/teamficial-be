package teamficial.teamficial_be.domain.keyword.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamficial.teamficial_be.domain.profile.entity.Profile;
import teamficial.teamficial_be.global.entity.BaseEntity;

@Entity
@Getter
@Builder
@Table(
        name = "head_keyword",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"profile_id", "keyword_name"})
        }
)
@AllArgsConstructor
@NoArgsConstructor
public class HeadKeyword extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "head_keyword_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Column(name = "keyword_name", length = 50)
    private String keywordName;

}
