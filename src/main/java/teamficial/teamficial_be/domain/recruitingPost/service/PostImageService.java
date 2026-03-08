package teamficial.teamficial_be.domain.recruitingPost.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public List<String> getPostImageUrls(Long postId) {
        List<PostImage> images =
                postImageRepository.findByRecruitingPostIdOrderByImageOrder(postId);

        return images.stream()
                .map(img -> preSignedUrlService.getPublicUrl(img.getObjectKey()))
                .toList();
    }

    @Transactional
    public void updatePostImages(RecruitingPost post, List<String> imageKeys) {

        Long postId = post.getId();

        // 기존 이미지 조회
        List<PostImage> existingImages =
                postImageRepository.findByRecruitingPostId(postId);

        // S3 삭제
        for (PostImage image : existingImages) {
            preSignedUrlService.deleteByKey(image.getObjectKey());
        }

        // DB 삭제
        postImageRepository.deleteAll(existingImages);

        // 새 이미지 저장
        if (imageKeys == null || imageKeys.isEmpty()) {
            return;
        }

        List<PostImage> images = new ArrayList<>();

        int order = 0;

        for (String key : imageKeys) {

            String newKey = preSignedUrlService.moveTempImageToPost(key, postId);

            PostImage image = PostImage.builder()
                    .recruitingPost(post)
                    .objectKey(newKey)
                    .imageOrder(order++)
                    .build();

            images.add(image);
        }

        postImageRepository.saveAll(images);
    }

}
