package teamficial.teamficial_be.domain.keyword.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import teamficial.teamficial_be.domain.keyword.entity.HeadKeyword;

@Getter
@Builder
public class HeadKeywordInfo {
    @Schema(description = "대표키워드 id", example = "1")
    private Long headKeywordId;

    @Schema(description = "키워드 이름", example = "성실한")
    private String headKeywordName;

    public static HeadKeywordInfo from(HeadKeyword headKeyword) {
        return HeadKeywordInfo.builder()
                .headKeywordId(headKeyword.getId())
                .headKeywordName(headKeyword.getKeywordName())
                .build();
    }
}