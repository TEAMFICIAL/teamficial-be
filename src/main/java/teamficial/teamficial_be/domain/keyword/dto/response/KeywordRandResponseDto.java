package teamficial.teamficial_be.domain.keyword.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeywordRandResponseDto {

    private String requesterUuid;
    private List<KeywordInfo> keywords;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeywordInfo {
        private String keywordName;
        private Integer count;
    }
}

