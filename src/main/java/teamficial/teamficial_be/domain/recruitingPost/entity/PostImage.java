package teamficial.teamficial_be.domain.recruitingPost.entity;

import jakarta.persistence.*;
import lombok.*;
import teamficial.teamficial_be.global.entity.BaseEntity;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post_image")
public class PostImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_image_id")
    private Long id;

    @Column(name = "object_key", nullable = false)
    private String objectKey;

    @Column(name = "image_order", nullable = false)
    private Integer imageOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiting_post_id", nullable = false)
    private RecruitingPost recruitingPost;
}