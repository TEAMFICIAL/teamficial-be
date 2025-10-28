package teamficial.teamficial_be.domain.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import teamficial.teamficial_be.domain.profile.entity.Profile;

@Getter
@Builder
public class ApplicantListResponseDto {
    private Long profileId;
    private String profileName;
    private String profilePosition;
    private String profileImage;

    public static ApplicantListResponseDto from(Profile profile) {
        return ApplicantListResponseDto.builder()
                .profileId(profile.getId())
                .profileName(profile.getProfileName())
                .profileName(profile.getProfileImage())
                .profilePosition(profile.getPosition().getDescription())
                .build();
    }
}
