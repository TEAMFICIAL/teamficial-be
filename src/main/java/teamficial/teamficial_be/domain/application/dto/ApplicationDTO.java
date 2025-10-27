package teamficial.teamficial_be.domain.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApplicationDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApplicationRequestDTO {
        private Long profileId;
        private Long recruitingPostId;
        private String content;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApplicationResponseDTO {
        private Long applicationId;
        private String status;
        private Long userId;
        private Long profileId;
        private Long recruitingPostId;
        private String message;
    }
}
