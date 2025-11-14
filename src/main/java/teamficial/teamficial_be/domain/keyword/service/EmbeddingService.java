package teamficial.teamficial_be.domain.keyword.service;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class EmbeddingService {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    public EmbeddingService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://clovastudio.stream.ntruss.com/v1/api-tools/embedding")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }

    private final WebClient webClient;

    public float[] embed(String text) {

        Map<String, Object> body = Map.of("text", text);

        Map<String, Object> response = webClient.post()
                .uri("/clir-emb-dolphin")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .header("X-NCP-CLOVASTUDIO-REQUEST-ID", UUID.randomUUID().toString())
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<String, Object> result = (Map<String, Object>) response.get("result");

        List<Double> vector = (List<Double>) result.get("embedding");

        float[] embedding = new float[vector.size()];
        for (int i = 0; i < vector.size(); i++) {
            embedding[i] = vector.get(i).floatValue();
        }

        return embedding;
    }
}