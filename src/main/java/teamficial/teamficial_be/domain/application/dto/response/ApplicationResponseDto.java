package teamficial.teamficial_be.domain.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import teamficial.teamficial_be.domain.application.entity.Application;
import teamficial.teamficial_be.domain.profile.dto.response.ProfileResponseDto;

@Getter
@Builder
public class ApplicationResponseDto {
    private Long id;
    private String content;
    private ProfileResponseDto profile;

    public static ApplicationResponseDto from(Application application) {
        return ApplicationResponseDto.builder()
                .content(application.getContent())
                .profile(ProfileResponseDto.of(application.getProfile()))
                .build();
    }
}
