package teamficial.teamficial_be.domain.profile.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import teamficial.teamficial_be.domain.profile.entity.Profile;
import teamficial.teamficial_be.domain.profile.entity.WorkingTime;
import teamficial.teamficial_be.global.enums.Position;

import java.time.LocalDateTime;

@Builder
public class ProfileResponseDto {
    @Schema(description = "프로필 id", example="1")
    private Long profileId;
    @Schema(description = "유저 id", example="1")
    private Long userId;
    @Schema(description = "유저 이름", example="연호")
    private String userName;
    private String profileImageUrl;
    @Schema(description = "프로필 이름", example="1")
    private String profileName;
    @Schema(description = "프로필 파트", example="FRONTEND")
    private Position position;
    @Schema(description = "사용자 id", example="MORNING")
    private WorkingTime workingTime;
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
                .position(profile.getPosition())
                .workingTime(profile.getWorkingTime())
                .link(profile.getLink())
                .contactWay(profile.getContactWay())
                .createdAt(profile.getCreatedAt())
                .modifiedAt(profile.getUpdatedAt())
                .build();
    }
}
