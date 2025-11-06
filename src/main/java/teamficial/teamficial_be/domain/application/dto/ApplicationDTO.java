package teamficial.teamficial_be.domain.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamficial.teamficial_be.global.enums.Position;

@Getter
@NoArgsConstructor
public class ApplicationDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApplicationRequestDTO {
        @Schema(description = "프로필 Id", example="1")
        @NotBlank(message = "프로필 이름은 필수입니다.")
        private Long profileId;
        @Schema(description = "모집 글 Id", example="1")
        @NotBlank(message = "모집 글 Id는 필수입니다.")
        private Long recruitingPostId;
        @Schema(description = "지원 직무", example="FRONTEND")
        @NotBlank(message = "모집 글 Id는 필수입니다.")
        private Position position;
        @Schema(description = "지원 내용", example="...")
        private String content;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApplicationResponseDTO {
        private Long applicationId;
        private String status;
        private Position position;
        private Long userId;
        private Long profileId;
        private Long recruitingPostId;
        private String message;
    }
}
