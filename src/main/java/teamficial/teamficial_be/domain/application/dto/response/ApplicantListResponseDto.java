package teamficial.teamficial_be.domain.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import teamficial.teamficial_be.domain.profile.entity.Profile;
import teamficial.teamficial_be.global.enums.Position;

import java.util.List;

@Getter
@Builder
@Slf4j
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
    @Schema(description = "대표 키워드 리스트")
    private List<String> keywordList;


    public static ApplicantListResponseDto from(
            Long applicationId,
            String applicationStatus,
            Long profileId,
            String applicantName,
            String profileImage,
            Position position,
            List<String> keywordList
    ) {

        log.info("ApplicantListResponseDto");

        return ApplicantListResponseDto.builder()
                .applicationId(applicationId)
                .profileId(profileId)
                .applicantName(applicantName)
                .profileImage(profileImage)
                .profilePosition(position.getDescription())
                .applicationStatus(applicationStatus)
                .keywordList(keywordList)
                .build();
    }
}
