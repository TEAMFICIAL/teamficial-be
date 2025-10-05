package teamficial.teamficial_be.global.apiPayload.exception.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.global.redis.RedisService;
import teamficial.teamficial_be.global.security.dto.TokenResponse;
import teamficial.teamficial_be.global.security.jwt.CookieUtil;
import teamficial.teamficial_be.global.security.jwt.TokenProvider;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;
    private final RedisService redisService;
    private final CookieUtil cookieUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        User user = (User) oauthUser.getAttributes().get("user");

        // 토큰 생성
        TokenResponse tokens = tokenProvider.createToken(user.getId());

        // Redis에 저장
        redisService.setRefreshToken(user.getId(), tokens.getRefreshToken());

        // 쿠키 세팅
        Cookie refreshCookie = cookieUtil.createCookie(tokens.getRefreshToken());
        response.addCookie(refreshCookie);

        response.setHeader("Authorization", "Bearer " + tokens.getAccessToken());

    }
}
