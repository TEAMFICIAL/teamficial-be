package teamficial.teamficial_be.domain.keyword.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KeywordListResponseDto {
    String keyword;
    boolean isHead;
}
