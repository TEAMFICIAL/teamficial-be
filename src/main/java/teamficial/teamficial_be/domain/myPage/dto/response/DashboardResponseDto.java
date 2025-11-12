package teamficial.teamficial_be.domain.myPage.dto.response;

import lombok.Builder;
import lombok.Getter;
import teamficial.teamficial_be.domain.application.entity.Application;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;

import java.util.List;

@Getter
@Builder
public class DashboardResponseDto {
    List<MyApplicationResponseDto> myApplications;
    List<CurrentApplicantResponseDto> myRecruitingPost;

}
