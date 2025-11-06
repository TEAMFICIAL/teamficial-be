package teamficial.teamficial_be.domain.keyword.dto.response;

import lombok.Builder;
import lombok.Getter;
import teamficial.teamficial_be.domain.keyword.entity.HeadKeyword;
import teamficial.teamficial_be.domain.profile.entity.Profile;

import java.util.List;

@Getter
@Builder
public class HeadKeywordResponseDto {
    Long profileId;
    String profileName;
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
