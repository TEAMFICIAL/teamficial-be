package teamficial.teamficial_be.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import teamficial.teamficial_be.domain.application.dto.response.ApplicantListResponseDto;
import teamficial.teamficial_be.domain.application.entity.Application;
import teamficial.teamficial_be.domain.recruitingPost.dto.response.RecruitingPostResponseDto;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;

import java.util.List;


@Builder
@Getter
public class CurrentApplicationResponseDto {
    private RecruitingPostResponseDto recruitingPost;
    private List<ApplicantListResponseDto> applicantList;

    public static CurrentApplicationResponseDto from(RecruitingPost recruitingPost, List<Application> applications) {
        return CurrentApplicationResponseDto.builder()
                .recruitingPost(RecruitingPostResponseDto.from(recruitingPost))
                .applicantList(applications.stream()
                        .map(application -> ApplicantListResponseDto.from(application.getProfile()))
                        .toList()
                )
                .build();
    }
}
