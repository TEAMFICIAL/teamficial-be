package teamficial.teamficial_be.domain.myPage.dto.response;

import lombok.Builder;
import lombok.Getter;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class MyApplicationResponseDto {
    private Long recruitingPostId;
    private String writerName;
    private String title;
    private List<String> tags;
    private String status;
    private String period;
    private LocalDateTime deadline;
    private LocalDateTime createdAt;

    public static MyApplicationResponseDto of(RecruitingPost recruitingPost) {
        List<String> tags = recruitingPost.getRecruitingDetails().stream()
                .map(recruitingDetail -> recruitingDetail.getPosition().getDescription())
                .toList();

        return MyApplicationResponseDto.builder()
                .recruitingPostId(recruitingPost.getId())
                .title(recruitingPost.getTitle())
                .writerName(recruitingPost.getProfile().getProfileName())
                .status(recruitingPost.getStatus().getDescription())
                .tags(tags)
                .period(recruitingPost.getPeriod().getDescription())
                .deadline(recruitingPost.getDeadline())
                .createdAt(recruitingPost.getCreatedAt())
                .build();
    }
}
