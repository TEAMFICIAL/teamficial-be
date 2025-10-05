package teamficial.teamficial_be.global.security.jwt;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    @Value("${jwt.token.refresh-expiration-time}")
    private long refreshExpirationTime;

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    public Cookie createCookie(String refreshToken) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) refreshExpirationTime/1000);

        return cookie;
    }

}
