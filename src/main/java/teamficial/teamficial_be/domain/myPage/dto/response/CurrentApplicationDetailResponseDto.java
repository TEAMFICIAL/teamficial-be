package teamficial.teamficial_be.domain.myPage.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import teamficial.teamficial_be.domain.application.dto.response.ApplicantListResponseDto;
import teamficial.teamficial_be.domain.application.entity.Application;
import teamficial.teamficial_be.domain.application.entity.ApplicationStatus;
import teamficial.teamficial_be.domain.profile.entity.Profile;
import teamficial.teamficial_be.domain.recruitingPost.dto.response.RecruitingPostResponseDto;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Builder
@Getter
@Slf4j
public class CurrentApplicationDetailResponseDto {
    private RecruitingPostResponseDto recruitingPost;
    private List<ApplicantListResponseDto> applicantList;

    public static CurrentApplicationDetailResponseDto from(RecruitingPost recruitingPost, List<Application> applications, long dDay, Map<Long, List<String>> keywordMap) {

        log.info("CurrentApplicationDetailResponseDto");

        return CurrentApplicationDetailResponseDto.builder()
                .recruitingPost(RecruitingPostResponseDto.from(recruitingPost,dDay))
                .applicantList(applications.stream()
                        .map(application -> {
                                ApplicationStatus status = application.getApplicationStatus();
                                if (status == ApplicationStatus.TEMP_SAVED){
                                    status = ApplicationStatus.MATCHED;
                                }

                            Profile profile = application.getProfile();

                            return ApplicantListResponseDto.from(
                                    application.getId(),
                                    status.getDescription(),
                                    profile.getId(),
                                    profile.getUserName(),
                                    profile.getProfileImage(),
                                    application.getPosition(),
                                    keywordMap.getOrDefault(profile.getId(), List.of())
                            );
                        })
                        .toList()
                )
                .build();
    }
}
