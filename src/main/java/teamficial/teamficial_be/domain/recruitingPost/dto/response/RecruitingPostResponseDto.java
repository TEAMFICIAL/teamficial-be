package teamficial.teamficial_be.domain.recruitingPost.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class RecruitingPostResponseDto {
    @Schema(description = "모집 글 id", example="1")
    private Long recruitingPostId;
    @Schema(description = "모집 글 제목", example="팀피셜 팀원 모집합니다.")
    private String recruitingPostTitle;
    @Schema(description = "모집 글 본문", example="프로젝트 소개\n" +
            "‘팀피셜’은 함께 프로젝트를 진행한 동료들이 남긴 피드백을 기반으로, 나의 협업 스타일과 강점을 시각화해주는 서비스입니다.~~~")
    private String recruitingPostContent;
    @Schema(description = "진행 방식", example="온라인")
    private String progressWay;
    @Schema(description = "연락 방식", example="{오픈채팅 링크}")
    private String contactWay;
    @Schema(description = "시작 예정일", example="2025-10-28")
    private LocalDate startDate;
    @Schema(description = "프로젝트 진행 기간", example="1개월 이내")
    private String period;
    @Schema(description = "공고 마감일", example="2025-10-28T06:38:03.179Z")
    private LocalDate deadline;
    @Schema(description = "모집 분야/인원", example="[\"프론트엔드 3명\",\n\"백엔드 3명\"]")
    private List<String> recruitingDetails;
    private long dDay;

    public static RecruitingPostResponseDto from(RecruitingPost recruitingPost, long dDay) {
        return RecruitingPostResponseDto.builder()
                .recruitingPostId(recruitingPost.getId())
                .recruitingPostTitle(recruitingPost.getTitle())
                .recruitingPostContent(recruitingPost.getContent())
                .period(recruitingPost.getPeriod().getDescription())
                .progressWay(recruitingPost.getProgressWay().getDescription())
                .contactWay(recruitingPost.getContactWay())
                .startDate(recruitingPost.getStartDate())
                .deadline(recruitingPost.getDeadline())
                .recruitingDetails(recruitingPost.getRecruitingDetails().stream()
                        .map(rd -> rd.getRecruitingDetail())
                        .toList()
                )
                .dDay(dDay)
                .build();
    }
}
