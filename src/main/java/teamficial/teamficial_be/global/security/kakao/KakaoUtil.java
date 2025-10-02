package teamficial.teamficial_be.global.security.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

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
        String response = restClient.post()
                        .uri(uriBuilder -> uriBuilder
                                .scheme("https")
                                .host("kauth.kakao.com")
                                .path("/oauth/token")
                                .queryParam("grant_type", "authorization_code")
                                .queryParam("client_id",client)
                                .queryParam("code", accessCode)
                                .queryParam("redirect_uri", redirect)
                                .queryParam("client_secret", clientSecret)
                                .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .retrieve()
                .body(String.class);

        try {
            return objectMapper.readValue(response, KakaoDTO.OAuthToken.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse Kakao access token", e);
        }
    }

    public KakaoDTO.KakaoProfile requestProfile(KakaoDTO.OAuthToken oAuthToken) {
        RestTemplate restTemplate2 = new RestTemplate();
        HttpHeaders headers2 = new HttpHeaders();

        String response = restClient.get()
                .uri(userInfoUri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + oAuthToken.getAccess_token())
                .retrieve()
                .body(String.class);

        try {
            KakaoDTO.KakaoProfile kakaoProfile = objectMapper.readValue(response, KakaoDTO.KakaoProfile.class);
            return kakaoProfile;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse Kakao access token", e);
        }
    }

}
