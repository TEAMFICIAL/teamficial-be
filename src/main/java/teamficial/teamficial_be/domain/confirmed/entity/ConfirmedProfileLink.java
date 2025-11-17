package teamficial.teamficial_be.domain.confirmed.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmedProfileLink {
    @Id @Column(name = "confirmed_profile_link_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "confirmed_profile_id", nullable = false)
    private ConfirmedProfile confirmedProfile;

    @Column(name = "link", length = 255)
    private String link;
}
