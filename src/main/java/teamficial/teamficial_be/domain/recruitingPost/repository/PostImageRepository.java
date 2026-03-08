package teamficial.teamficial_be.domain.recruitingPost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamficial.teamficial_be.domain.recruitingPost.entity.PostImage;

import java.util.List;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    List<PostImage> findByRecruitingPostId(Long postId);

    List<PostImage> findByRecruitingPostIdOrderByImageOrder(Long postId);
}
