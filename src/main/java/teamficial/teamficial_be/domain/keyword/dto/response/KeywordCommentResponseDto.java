package teamficial.teamficial_be.domain.keyword.dto.response;

import lombok.Builder;
import lombok.Getter;
import teamficial.teamficial_be.domain.keyword.entity.KeywordComment;

import java.time.LocalDateTime;

@Getter
@Builder
public class KeywordCommentResponseDto {
    String comment;
    LocalDateTime createdAt;

    public static KeywordCommentResponseDto from(KeywordComment keywordComment) {
        return KeywordCommentResponseDto.builder()
                .comment(keywordComment.getContent())
                .createdAt(keywordComment.getCreatedAt())
                .build();
    }
}
