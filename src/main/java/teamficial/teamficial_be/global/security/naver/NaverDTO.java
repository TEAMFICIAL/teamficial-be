package teamficial.teamficial_be.global.security.naver;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NaverDTO {
    @Getter
    @Setter
    public static class OAuthToken {
        private String access_token;
        private String refresh_token;
        private String token_type;
        private String expires_in;
        private String error;
        private String error_description;
    }

    @Getter
    @Setter
    public static class NaverProfile {
        private String resultcode;
        private String message;
        private Response response;

        @Getter
        @Setter
        public static class Response {
            private String id;
            private String email;
            private String name;
            private String nickname;
            private String profile_image;
        }
    }
}
