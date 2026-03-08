package teamficial.teamficial_be.domain.recruitingPost.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamficial.teamficial_be.domain.profile.service.PreSignedUrlService;
import teamficial.teamficial_be.domain.recruitingPost.entity.PostImage;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;
import teamficial.teamficial_be.domain.recruitingPost.repository.PostImageRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostImageService {
    private final PostImageRepository postImageRepository;
    private final PreSignedUrlService preSignedUrlService;

    public void saveImages(RecruitingPost post, List<String> imageKeys) {
        List<PostImage> images = new ArrayList<>();
        int order = 0;

        for (String key : imageKeys) {

            String newKey = preSignedUrlService.moveTempImageToPost(key, post.getId());

            PostImage image = PostImage.builder()
                    .recruitingPost(post)
                    .objectKey(newKey)
                    .imageOrder(order++)
                    .build();
            images.add(image);
        }

        postImageRepository.saveAll(images);
    }

    public void deleteImagesByPostId(Long postId) {

        List<PostImage> images = postImageRepository.findByRecruitingPostId(postId);

        for (PostImage image : images) {
            preSignedUrlService.deleteByKey(image.getObjectKey());
        }
    }
}
