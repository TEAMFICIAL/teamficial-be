package teamficial.teamficial_be.global.security.google.dto;

import lombok.Getter;

public class GoogleDTO {

    @Getter
    public static class OAuthToken{
        private String access_token;
        private String refresh_token;
        private int expires_in;
        private String scope;
        private String token_type;
        private String id_token;
    }

    @Getter
    public static class GoogleProfile{
        private String sub;
        private String name;
        private String given_name;
        private String family_name;
        private String picture;
        private String email;
        private boolean email_verified;
        private String locale;
    }
}
