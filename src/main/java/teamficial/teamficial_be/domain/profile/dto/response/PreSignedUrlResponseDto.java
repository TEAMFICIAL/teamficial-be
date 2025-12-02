package teamficial.teamficial_be.domain.profile.dto.response;


import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class PreSignedUrlResponseDto {
    private String preSignedUrl;
    private String objectKey;
    private Date expiresAt;

    public static PreSignedUrlResponseDto of(String preSignedUrl, String objectKey, Date expiresAt) {
        return PreSignedUrlResponseDto.builder()
                .preSignedUrl(preSignedUrl)
                .objectKey(objectKey)
                .expiresAt(expiresAt)
                .build();
    }
}
