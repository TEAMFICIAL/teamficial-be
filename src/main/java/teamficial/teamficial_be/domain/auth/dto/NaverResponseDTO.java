package teamficial.teamficial_be.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


public class NaverResponseDTO {
    @Getter
    @AllArgsConstructor
    public static class LoginTokenResponseDto {
        @Schema(description = "사용자 id", example="1")
        private Long userId;
        @Schema(description = "사용자 accessToken", example="abc123...")
        private String accessToken;
        @Schema(description = "사용자 refreshToken", example="xyz456...")
        private String refreshToken;
        @Schema(description = "최초 로그인 여부", example="true")
        private boolean isFirst;

        public static LoginTokenResponseDto of(Long userId, String accessToken, String refreshToken, boolean isFirst) {
            return new LoginTokenResponseDto(
                    userId,
                    accessToken,
                    refreshToken,
                    isFirst);
        }
    }
}
