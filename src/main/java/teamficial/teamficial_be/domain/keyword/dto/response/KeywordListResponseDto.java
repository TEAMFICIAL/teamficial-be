package teamficial.teamficial_be.domain.keyword.dto.response;

import lombok.Builder;
import lombok.Getter;
import teamficial.teamficial_be.domain.keyword.entity.Keyword;

@Getter
@Builder
public class KeywordListResponseDto {
    Long keywordId;
    String keywordName;
    boolean isHead;
    int count;

    public static KeywordListResponseDto from(Keyword keyword) {
        return KeywordListResponseDto.builder()
                .keywordId(keyword.getId())
                .keywordName(keyword.getKeywordName())
                .isHead(keyword.is_head())
                .count(keyword.getCount())
                .build();
    }
}
