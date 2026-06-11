package teamficial.teamficial_be.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class LoginResponseDTO {

    @Getter
    @AllArgsConstructor
    public static class LoginTokenResponseDto {
        @Schema(description = "사용자 id", example="1")
        private Long userId;
        @Schema(description = "사용자 이름", example="연호")
        private String userName;
        @Schema(description = "사용자 accessToken", example="exksoijsdjon...")
        private String accessToken;
        @Schema(description = "사용자 refreshToken", example="exjnasoicjkdd...")
        private String refreshToken;
        @Schema(description = "사용자 uuid",example = "8dfddc39-2fdb-40c6-b0c4-a47d50c1a9bb")
        private String uuid;
        @Schema(description = "사용자 최초 로그인 여부(처음이면 true)", example="true")
        private boolean isFirst;

        public static LoginTokenResponseDto of(Long userId,String userName,String accessToken,String refreshToken,String uuid,boolean isFirst) {
            return new LoginTokenResponseDto(
                    userId,
                    userName,
                    accessToken,
                    refreshToken,
                    uuid,
                    isFirst);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class RecreateTokenResponseDto {
        @Schema(description = "사용자 id", example="1")
        private Long userId;
        @Schema(description = "사용자 accessToken", example="exksoijsdjon...")
        private String accessToken;
        @Schema(description = "사용자 refreshToken", example="exjnasoicjkdd...")
        private String refreshToken;

        public static RecreateTokenResponseDto of(Long userId,String accessToken,String refreshToken) {
            return new RecreateTokenResponseDto(
                    userId,
                    accessToken,
                    refreshToken);
        }
    }
}
