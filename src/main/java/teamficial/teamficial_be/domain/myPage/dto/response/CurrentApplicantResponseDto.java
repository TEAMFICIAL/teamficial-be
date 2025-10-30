package teamficial.teamficial_be.domain.myPage.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CurrentApplicantResponseDto {
    @Schema(description = "모집 글 id", example="1")
    private Long recruitingPostId;
    @Schema(description = "작성자 이름", example="연호")
    private String writerName;
    @Schema(description = "작성자 프로필 사진")
    private String profileImage;
    @Schema(description = "모집 글 제목", example="팀피셜 팀원 모집합니다.")
    private String title;
    @Schema(description = "작성 글의 모집 파트", example="[\"프론트엔드\",\n\"백엔드\"]")
    private List<String> tags;
    @Schema(description = "공고 마감일", example="2025-10-28T06:38:03.179Z")
    private LocalDate deadline;
    @Schema(description = "총 지원자 수", example="3")
    private int totalApplicants;
    @Schema(description = "글 작성일", example="2025-10-28T06:38:03.179Z")
    private LocalDateTime createdAt;

    public static CurrentApplicantResponseDto of(RecruitingPost recruitingPost) {
        List<String> tags = recruitingPost.getRecruitingDetails().stream()
                .map(recruitingDetail -> recruitingDetail.getPosition().getDescription())
                .toList();

        return CurrentApplicantResponseDto.builder()
                .recruitingPostId(recruitingPost.getId())
                .title(recruitingPost.getTitle())
                .profileImage(recruitingPost.getProfile().getProfileImage())
                .writerName(recruitingPost.getProfile().getUserName())
                .totalApplicants(recruitingPost.getTotalApplicants())
                .tags(tags)
                .deadline(recruitingPost.getDeadline())
                .createdAt(recruitingPost.getCreatedAt())
                .build();
    }
}
