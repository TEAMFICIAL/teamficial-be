package teamficial.teamficial_be.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import teamficial.teamficial_be.domain.auth.dto.KakaoResponseDTO;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.domain.user.entity.UserRole;
import teamficial.teamficial_be.domain.user.repository.UserRepository;
import teamficial.teamficial_be.global.security.dto.TokenResponse;
import teamficial.teamficial_be.global.security.jwt.TokenProvider;
import teamficial.teamficial_be.global.security.kakao.KakaoDTO;
import teamficial.teamficial_be.global.security.kakao.KakaoUtil;
import teamficial.teamficial_be.global.redis.RedisService;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final KakaoUtil kakaoUtil;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;

    public KakaoResponseDTO.LoginTokenResponseDto kakaoLogin(String accessCode, String redirectUri){
        KakaoDTO.OAuthToken oAuthToken = kakaoUtil.requestToken(accessCode,redirectUri);
        KakaoDTO.KakaoProfile kakaoProfile = kakaoUtil.requestProfile(oAuthToken);

        String email = kakaoProfile.getKakao_account().getEmail();
        String name = kakaoProfile.getProperties().getNickname();

        AtomicBoolean isFirst = new AtomicBoolean(false);

        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .map(existingUser -> existingUser)
                .orElseGet(() -> {
                    isFirst.set(true);
                    return createUser(email, name);
                });

        TokenResponse tokenResponse = tokenProvider.createToken(user);
        redisService.setRefreshToken(user.getEmail(),tokenResponse.getRefreshToken());

        return KakaoResponseDTO.LoginTokenResponseDto.of(user.getId(),tokenResponse.getAccessToken(),tokenResponse.getRefreshToken(),isFirst.get());
    }

    private User createUser(String email, String name) {
        String rawPassword = UUID.randomUUID().toString();

        User user =User.builder()
                .email(email)
                .name(name)
                .password(passwordEncoder.encode(rawPassword))
                .userRole(UserRole.USER)
                .build();

        return userRepository.save(user);
    }

}
