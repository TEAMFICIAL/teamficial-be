package teamficial.teamficial_be.domain.profile.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import teamficial.teamficial_be.domain.keyword.entity.HeadKeyword;
import teamficial.teamficial_be.domain.profile.entity.Profile;
import teamficial.teamficial_be.domain.profile.entity.ProfileLink;
import teamficial.teamficial_be.domain.profile.entity.WorkingTime;
import teamficial.teamficial_be.global.enums.Position;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    @Schema(description = "근무 시간대", example="아침")
    private String workingTime;
    @Schema(description = "관련 링크")
    private List<String> links;
    @Schema(description = "연락 수단", example="오픈채팅방 링크")
    private String contactWay;
    @Schema(description = "대표 키워드")
    private List<String> headKeywords;

    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Schema(description = "uuid")
    private String uuid;

    public static ProfileResponseDto of(Profile profile, List<HeadKeyword> headKeywords) {
        List<String> linkList = profile.getProfileLinks().stream()
                .map(ProfileLink::getLink)
                .collect(Collectors.toList());

        String workingTimeDesc =
                profile.getWorkingTime() != null ? profile.getWorkingTime().getDescription() : null;

        return ProfileResponseDto.builder()
                .profileId(profile.getId())
                .userId(profile.getUser().getId())
                .userName(profile.getUserName())
                .profileName(profile.getProfileName())
                .profileImageUrl(profile.getProfileImage())
                .workingTime(workingTimeDesc)
                .links(linkList)
                .contactWay(profile.getContactWay())
                .isDeleted(profile.isDeleted())
                .createdAt(profile.getCreatedAt())
                .modifiedAt(profile.getUpdatedAt())
                .headKeywords(headKeywords.stream()
                        .map(HeadKeyword::getKeywordName)
                        .toList()
                )
                .uuid(profile.getUser().getUuid())
                .build();
    }
}
