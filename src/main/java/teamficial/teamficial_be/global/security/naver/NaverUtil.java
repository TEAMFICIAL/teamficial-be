package teamficial.teamficial_be.global.security.naver;

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
public class NaverUtil {

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String userInfoUri;

    private final RestClient restClient = RestClient.create();
    private final ObjectMapper objectMapper;

    /**
     * 인가 코드로 액세스 토큰 요청
     */
    public NaverDTO.OAuthToken requestToken(String code, String state, String redirectUri) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        params.add("state", state);

        String response = restClient.post()
                .uri("https://nid.naver.com/oauth2.0/token")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(params)
                .retrieve()
                .body(String.class);

        try {
            return objectMapper.readValue(response, NaverDTO.OAuthToken.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse Naver access token", e);
        }
    }

    /**
     * 액세스 토큰으로 사용자 프로필 요청
     */
    public NaverDTO.NaverProfile requestProfile(NaverDTO.OAuthToken oAuthToken) {
        String response = restClient.get()
                .uri(userInfoUri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + oAuthToken.getAccess_token())
                .retrieve()
                .body(String.class);

        try {
            return objectMapper.readValue(response, NaverDTO.NaverProfile.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse Naver user profile", e);
        }
    }
}
