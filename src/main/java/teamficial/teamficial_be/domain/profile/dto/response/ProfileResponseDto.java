package teamficial.teamficial_be.domain.profile.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import teamficial.teamficial_be.domain.profile.entity.Profile;


import java.time.LocalDateTime;

@Getter
@Builder
public class ProfileResponseDto {
    @Schema(description = "프로필 id", example="1")
    private Long profileId;
    @Schema(description = "유저 id", example="1")
    private Long userId;
    @Schema(description = "유저 이름", example="연호")
    private String userName;
    @Schema(description = "프로필 사진")
    private String profileImageUrl;
    @Schema(description = "프로필 이름", example="1")
    private String profileName;
    @Schema(description = "프로필 파트", example="프론트엔드")
    private String position;
    @Schema(description = "사용자 id", example="아침")
    private String workingTime;
    @Schema(description = "관련 링크")
    private String link;
    @Schema(description = "연락 수단", example="오픈채팅방 링크")
    private String contactWay;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static ProfileResponseDto of(Profile profile) {
        return ProfileResponseDto.builder()
                .profileId(profile.getId())
                .userId(profile.getUser().getId())
                .userName(profile.getUserName())
                .profileName(profile.getProfileName())
                .profileImageUrl(profile.getProfileImage())
                .position(profile.getPosition().getDescription())
                .workingTime(profile.getWorkingTime().getDescription())
                .link(profile.getLink())
                .contactWay(profile.getContactWay())
                .createdAt(profile.getCreatedAt())
                .modifiedAt(profile.getUpdatedAt())
                .build();
    }
}
