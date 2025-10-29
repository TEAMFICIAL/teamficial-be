package teamficial.teamficial_be.domain.myPage.dto.response;

import lombok.Builder;
import lombok.Getter;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CurrentApplicantResponseDto {
    private Long recruitingPostId;
    private String writerName;
    private String title;
    private List<String> tags;
    private LocalDateTime deadline;
    private int totalApplicants;
    private LocalDateTime createdAt;

    public static CurrentApplicantResponseDto of(RecruitingPost recruitingPost) {
        List<String> tags = recruitingPost.getRecruitingDetails().stream()
                .map(recruitingDetail -> recruitingDetail.getPosition().getDescription())
                .toList();

        return CurrentApplicantResponseDto.builder()
                .recruitingPostId(recruitingPost.getId())
                .title(recruitingPost.getTitle())
                .writerName(recruitingPost.getProfile().getProfileName())
                .totalApplicants(recruitingPost.getTotalApplicants())
                .tags(tags)
                .deadline(recruitingPost.getDeadline())
                .createdAt(recruitingPost.getCreatedAt())
                .build();
    }
}
