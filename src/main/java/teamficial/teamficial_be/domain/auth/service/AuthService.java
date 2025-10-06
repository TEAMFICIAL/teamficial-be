package teamficial.teamficial_be.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamficial.teamficial_be.domain.auth.dto.LoginResponseDTO;
import teamficial.teamficial_be.domain.user.entity.LoginType;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.domain.user.entity.UserRole;
import teamficial.teamficial_be.domain.user.repository.UserRepository;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;
import teamficial.teamficial_be.global.security.dto.TokenResponse;
import teamficial.teamficial_be.global.security.dto.TokenResponseDTO;
import teamficial.teamficial_be.global.security.google.GoogleUtil;
import teamficial.teamficial_be.global.security.google.GoogleDTO;
import teamficial.teamficial_be.global.security.jwt.CookieUtil;
import teamficial.teamficial_be.global.security.jwt.TokenProvider;
import teamficial.teamficial_be.global.security.kakao.KakaoDTO;
import teamficial.teamficial_be.global.security.kakao.KakaoUtil;
import teamficial.teamficial_be.global.redis.RedisService;
import teamficial.teamficial_be.global.security.naver.NaverDTO;
import teamficial.teamficial_be.global.security.naver.NaverUtil;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final KakaoUtil kakaoUtil;
    private final GoogleUtil googleUtil;
    private final NaverUtil naverUtil;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;

    private static final String REFRESH_TOKEN_PREFIX = "refresh:";

    @Value("${jwt.token.refresh-expiration-time}")
    private long refreshExpirationTime;

    public LoginResponseDTO.LoginTokenResponseDto kakaoLogin(String accessCode, String redirectUri) {
        KakaoDTO.OAuthToken oAuthToken = kakaoUtil.requestToken(accessCode,redirectUri);
        KakaoDTO.KakaoProfile kakaoProfile = kakaoUtil.requestProfile(oAuthToken);

        String email = kakaoProfile.getKakao_account().getEmail();
        String name = kakaoProfile.getProperties().getNickname();

        return loginUser(email,name,LoginType.KAKAO);
    }

    public LoginResponseDTO.LoginTokenResponseDto googleLogin(String accessCode, String redirectUri) {
        GoogleDTO.OAuthToken oAuthToken = googleUtil.requestToken(accessCode, redirectUri);
        GoogleDTO.GoogleProfile googleProfile = googleUtil.getProfile(oAuthToken);

        String email = googleProfile.getEmail();
        String name = googleProfile.getName();

        return loginUser(email,name,LoginType.GOOGLE);
    }

    public LoginResponseDTO.LoginTokenResponseDto naverLogin(String accessCode, String state, String redirectUri) {
        NaverDTO.OAuthToken oAuthToken = naverUtil.requestToken(accessCode, state, redirectUri);
        NaverDTO.NaverProfile naverProfile = naverUtil.requestProfile(oAuthToken);

        String email = naverProfile.getResponse().getEmail();
        String name = naverProfile.getResponse().getName();

        return loginUser(email,name,LoginType.NAVER);
    }

    public LoginResponseDTO.LoginTokenResponseDto loginUser(String email, String name, LoginType loginType) {
        AtomicBoolean isFirst = new AtomicBoolean(false);

        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .map(existingUser -> existingUser)
                .orElseGet(() -> {
                    isFirst.set(true);
                    return createUser(email, name, loginType);
                });

        TokenResponse tokenResponse = tokenProvider.createToken(user.getId());
        String key = REFRESH_TOKEN_PREFIX + user.getId();
        redisService.setValue(key, tokenResponse.getRefreshToken(), refreshExpirationTime);

        return LoginResponseDTO.LoginTokenResponseDto.of(
                user.getId(),
                tokenResponse.getAccessToken(),
                tokenResponse.getRefreshToken(),
                isFirst.get()
        );
    }

    public LoginResponseDTO.RecreateTokenResponseDto recreateToken(String refreshToken) {
        tokenProvider.validateToken(refreshToken);

        Long userId = Long.valueOf(tokenProvider.getUserIdFromToken(refreshToken));

        String key = REFRESH_TOKEN_PREFIX + userId;
        String storedToken = redisService.getValue(key);
        if (storedToken == null || !storedToken.equals(refreshToken)) {
            throw new GeneralException(ErrorStatus.TOKEN_INVALID);
        }

        TokenResponseDTO.RefreshTokenResponseDto newTokenDTO = tokenProvider.recreate(userId);
        redisService.setValue(key, newTokenDTO.getRefreshToken(),refreshExpirationTime);

        return LoginResponseDTO.RecreateTokenResponseDto.of(
                userId,
                newTokenDTO.getAccessToken(),
                newTokenDTO.getRefreshToken()
        );
    }


    public void logout(User user) {
        redisService.deleteValue(REFRESH_TOKEN_PREFIX + user.getId());
    }

    private User createUser(String email, String name, LoginType loginType) {
        String rawPassword = UUID.randomUUID().toString();

        User user =User.builder()
                .email(email)
                .name(name)
                .password(passwordEncoder.encode(rawPassword))
                .userRole(UserRole.USER)
                .loginType(loginType)
                .build();

        return userRepository.save(user);
    }
}
