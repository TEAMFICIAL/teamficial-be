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
    @Schema(description = "대표 키워드 리스트 (id + 이름)")
    private List<HeadKeywordInfo> headKeywords;
    
    public static HeadKeywordResponseDto fromKeyword(Profile profile, List<HeadKeyword> headKeywords) {
        List<HeadKeywordInfo> infos = headKeywords.stream()
                .map(HeadKeywordInfo::from)
                .toList();

        return HeadKeywordResponseDto.builder()
                .profileId(profile.getId())
                .profileName(profile.getProfileName())
                .headKeywords(infos)
                .build();
    }
}
