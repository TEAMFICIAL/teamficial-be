package teamficial.teamficial_be.domain.keyword.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class PromptLoadService {
    @Value("classpath:prompts/vector_summary.txt")
    private Resource vectorSummaryPrompt;

    public String loadVectorSummaryPrompt(String content) {
        try {
            String template = new String(
                    vectorSummaryPrompt.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            return template.formatted(content);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load prompt file", e);
        }
    }
}
