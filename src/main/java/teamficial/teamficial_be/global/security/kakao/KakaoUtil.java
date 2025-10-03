package teamficial.teamficial_be.global.security.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class KakaoUtil {
    @Value("${spring.security.oauth2.client.registration.kakao.client_id}")
    private String client;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String userInfoUri;

    private final RestClient restClient = RestClient.create();
    private final ObjectMapper objectMapper;

    public KakaoDTO.OAuthToken requestToken(String accessCode,String redirect) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", client);
        params.add("redirect_uri", redirect);
        params.add("code", accessCode);
        params.add("client_secret", clientSecret);

        String response = restClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(params)
                .retrieve()
                .body(String.class);

        try {
            return objectMapper.readValue(response, KakaoDTO.OAuthToken.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse Kakao access token", e);
        }
    }

    public KakaoDTO.KakaoProfile requestProfile(KakaoDTO.OAuthToken oAuthToken) {
        String response = restClient.get()
                .uri(userInfoUri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + oAuthToken.getAccess_token())
                .retrieve()
                .body(String.class);

        try {
            KakaoDTO.KakaoProfile kakaoProfile = objectMapper.readValue(response, KakaoDTO.KakaoProfile.class);
            return kakaoProfile;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse Kakao user Profile response", e);
        }
    }

}
