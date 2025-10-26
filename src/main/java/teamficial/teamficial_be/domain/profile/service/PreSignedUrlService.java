package teamficial.teamficial_be.domain.profile.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;

import java.net.URL;
import java.util.Date;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class PreSignedUrlService {

    private final AmazonS3 amazonS3;

    @Value("${ncp.bucket-name}")
    private String bucketName;

    public String getPreSignedUrl(String imageName) {
        String fileName = createPath(imageName);

        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(bucketName, fileName);
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String bucket, String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, fileName)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(getPreSignedUrlExpiration());
        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicRead.toString());

        generatePresignedUrlRequest.addRequestParameter("x-amz-acl", CannedAccessControlList.PublicRead.toString());

        return generatePresignedUrlRequest;
    }

    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 2;
        expiration.setTime(expTimeMillis);
        return expiration;
    }

    private String createFileId() {
        return UUID.randomUUID().toString();
    }

    private String createPath(String fileName) {
        String fileId = createFileId();
        String prefix = "teamficial";
        return String.format("%s/%s", prefix, fileId + fileName);
    }

    public String extractKeyFromUrl(String url) {
        String marker = bucketName + ".";
        int idx = url.indexOf(marker);
        if (idx < 0) {
            return url.substring(url.lastIndexOf('/') + 1);
        }
        return url.substring(url.indexOf('/', idx + marker.length()));
    }

    @Async
    public void deleteImageByPath(String imagePath) {
        try {
            amazonS3.deleteObject(bucketName, imagePath);
        } catch (Exception e) {
            log.error("이미지 삭제 실패: {}", e.getMessage(), e);
            throw new GeneralException(ErrorStatus.FAILED_IMAGE_DELETE);
        }
    }

    public String getPublicUrl(String objectKey) {
        return "https://" + bucketName + ".kr.object.ncloudstorage.com/" + objectKey;
    }
}
