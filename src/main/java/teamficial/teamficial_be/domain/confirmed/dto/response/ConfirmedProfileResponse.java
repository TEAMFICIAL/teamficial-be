package teamficial.teamficial_be.domain.confirmed.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamficial.teamficial_be.domain.confirmed.entity.ConfirmedProfile;
import teamficial.teamficial_be.domain.profile.entity.WorkingTime;
import teamficial.teamficial_be.global.enums.Position;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmedProfileResponse {
    private String userName;

    private String profileName;

    private String profileImage;

    private WorkingTime workingTime;

    private String contactWay;

    private Position position;

    private List<String> keywords;

    private List<String> links;

    public static ConfirmedProfileResponse from(ConfirmedProfile profile) {

        List<String> keywordList = profile.getConfirmedHeadKeywords().stream()
                .map(hk -> hk.getKeywordName())
                .toList();

        List<String> linkList = profile.getConfirmedProfileLinks().stream()
                .map(l -> l.getLink())
                .toList();

        return ConfirmedProfileResponse.builder()
                .userName(profile.getUserName())
                .profileName(profile.getProfileName())
                .profileImage(profile.getProfileImage())
                .workingTime(profile.getWorkingTime())
                .contactWay(profile.getContactWay())
                .position(profile.getPosition())
                .keywords(keywordList)
                .links(linkList)
                .build();
    }
}
