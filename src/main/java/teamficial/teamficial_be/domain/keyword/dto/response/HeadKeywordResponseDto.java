package teamficial.teamficial_be.domain.keyword.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import teamficial.teamficial_be.domain.keyword.entity.HeadKeyword;
import teamficial.teamficial_be.domain.profile.entity.Profile;

import java.util.List;

@Getter
@Builder
public class HeadKeywordResponseDto {
    @Schema(description = "프로필 id", example="1")
    Long profileId;
    @Schema(description = "프로필 이름", example="프로필1")
    String profileName;
    @Schema(description = "키워드 리스트")
    List<String> headKeywords;
    
    public static HeadKeywordResponseDto fromKeyword(Profile profile, List<HeadKeyword> headKeywords) {
        return HeadKeywordResponseDto.builder()
                .profileId(profile.getId())
                .profileName(profile.getProfileName())
                .headKeywords(headKeywords.stream()
                        .map(HeadKeyword::getKeywordName)
                        .toList()
                )
                .build();
    }
}
