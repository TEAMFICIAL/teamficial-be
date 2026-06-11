package teamficial.teamficial_be.domain.recruitingPost.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PostImageResponseDto {

    private String imageUrl;
    private String objectKey;

}