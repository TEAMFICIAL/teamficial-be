package teamficial.teamficial_be.domain.keyword.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import teamficial.teamficial_be.domain.keyword.entity.Keyword;

@Getter
@Builder
public class KeywordResponseDto {
    @Schema(description = "키워드 id", example="1")
    Long keywordId;
    @Schema(description = "키워드", example="친절한")
    String keywordName;
    @Schema(description = "대표키워드 여부", example="true")
    boolean isHead;
    @Schema(description = "키워드 추출 횟수", example="3")
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
