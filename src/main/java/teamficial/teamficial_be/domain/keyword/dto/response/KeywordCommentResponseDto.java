package teamficial.teamficial_be.domain.keyword.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import teamficial.teamficial_be.domain.keyword.entity.KeywordComment;

import java.time.LocalDateTime;

@Getter
@Builder
public class KeywordCommentResponseDto {
    @Schema(description = "코멘트 id", example = "1")
    Long commentId;
    @Schema(description = "코멘트 내용", example="너무 친절해요")
    String comment;
    @Schema(description = "코멘트 생성 시간")
    LocalDateTime createdAt;

    public static KeywordCommentResponseDto from(KeywordComment keywordComment) {
        return KeywordCommentResponseDto.builder()
                .commentId(keywordComment.getId())
                .comment(keywordComment.getContent())
                .createdAt(keywordComment.getCreatedAt())
                .build();
    }
}
