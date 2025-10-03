package teamficial.teamficial_be.global.security.google;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class GoogleUtil {

    private final ObjectMapper objectMapper;
    private final RestClient restClient = RestClient.create();

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    public GoogleDTO.OAuthToken requestToken(String accessCode, String redirectUri){
        String code = URLDecoder.decode(accessCode, StandardCharsets.UTF_8);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        params.add("client_secret", clientSecret);

        String response = restClient.post()
                .uri("https://oauth2.googleapis.com/token")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(params)
                .retrieve()
                .body(String.class);

        try {
            return objectMapper.readValue(response, GoogleDTO.OAuthToken.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse Google access token", e);
        }
    }

    public GoogleDTO.GoogleProfile getProfile(GoogleDTO.OAuthToken token){
        String response = restClient.get()
                .uri("https://www.googleapis.com/oauth2/v3/userinfo")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccess_token())
                .retrieve()
                .body(String.class);

        try {
            GoogleDTO.GoogleProfile googleProfile = objectMapper.readValue(response, GoogleDTO.GoogleProfile.class);
            return googleProfile;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse Google user profile response", e);
        }
    }

}
