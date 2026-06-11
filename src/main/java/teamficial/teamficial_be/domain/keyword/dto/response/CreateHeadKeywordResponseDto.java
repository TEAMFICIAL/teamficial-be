package teamficial.teamficial_be.domain.keyword.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import teamficial.teamficial_be.domain.profile.entity.Profile;

@Getter
@Builder
public class CreateHeadKeywordResponseDto {
    @Schema(description = "프로필 id", example="1")
    Long profileId;
    @Schema(description = "등록/수정된 대표 키워드 이름")
    String headKeyword;

    public static CreateHeadKeywordResponseDto fromKeyword(Profile profile, String headKeyword) {
        return CreateHeadKeywordResponseDto.builder()
                .profileId(profile.getId())
                .headKeyword(headKeyword)
                .build();
    }
}
