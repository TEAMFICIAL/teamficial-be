package teamficial.teamficial_be.domain.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import teamficial.teamficial_be.domain.profile.entity.Profile;
import teamficial.teamficial_be.global.enums.Position;

@Getter
@Builder
public class ApplicantListResponseDto {
    @Schema(description = "지원 id", example="1")
    private Long applicationId;
    @Schema(description = "프로필 id", example="1")
    private Long profileId;
    @Schema(description = "지원자 이름", example="연호")
    private String applicantName;
    @Schema(description = "지원자 파트", example="프론트엔드")
    private String profilePosition;
    @Schema(description = "지원자 프로필 사진")
    private String profileImage;
    @Schema(description = "지원 상태", example="매칭 성공")
    private String applicationStatus;

    public static ApplicantListResponseDto from(Long applicationId,String applicationStatus, Profile profile, Position position) {
        return ApplicantListResponseDto.builder()
                .applicationId(applicationId)
                .profileId(profile.getId())
                .applicantName(profile.getUserName())
                .profileImage(profile.getProfileImage())
                .profilePosition(position.getDescription())
                .applicationStatus(applicationStatus)
                .build();
    }
}
