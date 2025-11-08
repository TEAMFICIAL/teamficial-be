package teamficial.teamficial_be.domain.keyword.dto.response;

import lombok.Builder;
import lombok.Getter;
import teamficial.teamficial_be.domain.keyword.entity.Keyword;

@Getter
@Builder
public class KeywordResponseDto {
    Long keywordId;
    String keywordName;
    boolean isHead;
    int count;

    public static KeywordResponseDto from(Keyword keyword) {
        return KeywordResponseDto.builder()
                .keywordId(keyword.getId())
                .keywordName(keyword.getKeywordName())
                .isHead(keyword.is_head())
                .count(keyword.getCount())
                .build();
    }
}
