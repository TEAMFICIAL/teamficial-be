package teamficial.teamficial_be.domain.myPage.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class MyTeamResponseDto {
    @Schema(description = "모집 글 id", example="1")
    private Long postId;
    @Schema(description = "프로젝트 기간", example="3개월")
    private String period;
    @Schema(description = "모집글 제목", example="팀피셜 팀원 구해요")
    private String title;
    @Schema(description = "진행 방식", example="온라인")
    private String progressWay;
    @Schema(description = "작성 글의 모집 파트", example="[\"프론트엔드\",\n\"백엔드\"]")
    private List<String> tags;
    @Schema(description = "총 팀원 수", example="2")
    private int totalMembers;
    private LocalDateTime createAt;

    public static MyTeamResponseDto from(RecruitingPost recruitingPost,int totalMembers) {
        List<String> tags = recruitingPost.getRecruitingDetails().stream()
                .map(recruitingDetail -> recruitingDetail.getPosition().getDescription())
                .toList();

        return MyTeamResponseDto.builder()
                .postId(recruitingPost.getId())
                .period(recruitingPost.getPeriod().getDescription())
                .title(recruitingPost.getTitle())
                .progressWay(recruitingPost.getProgressWay().getDescription())
                .tags(tags)
                .totalMembers(totalMembers)
                .createAt(recruitingPost.getCreatedAt())
                .build();
    }
}
