package teamficial.teamficial_be.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import teamficial.teamficial_be.domain.user.entity.LoginType;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.domain.user.entity.UserRole;
import teamficial.teamficial_be.domain.user.repository.UserRepository;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        LoginType loginTypeEnum = resolveLoginType(provider);
        String email = oAuth2User.getAttribute("email");
        String username = oAuth2User.getAttribute("name");
        String rawPassword = UUID.randomUUID().toString();

        Optional<User> user = userRepository.findByEmailAndDeletedAtIsNull(email);

        if (user.isEmpty()) {
            User newUser = User.builder()
                    .email(email)
                    .userRole(UserRole.USER)
                    .loginType(loginTypeEnum)
                    .name(username)
                    .password(passwordEncoder.encode(rawPassword))
                    .build();

            userRepository.save(newUser);

            return new AuthDetails(newUser, oAuth2User.getAttributes());
        } else {
            return new AuthDetails(user.get(), oAuth2User.getAttributes());
        }

    }

    private LoginType resolveLoginType(String provider) {
        return switch (provider.toLowerCase()) {
            case "google" -> LoginType.GOOGLE;
            case "naver" -> LoginType.NAVER;
            case "kakao" -> LoginType.KAKAO;
            default -> throw new GeneralException(ErrorStatus.LOGIN_TYPE_INVALID);
        };
    }
}
