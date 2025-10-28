package teamficial.teamficial_be.domain.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamficial.teamficial_be.domain.application.dto.ApplicationDTO;
import teamficial.teamficial_be.domain.application.entity.Application;
import teamficial.teamficial_be.domain.application.entity.ApplicationStatus;
import teamficial.teamficial_be.domain.application.repository.ApplicationRepository;
import teamficial.teamficial_be.domain.profile.entity.Profile;
import teamficial.teamficial_be.domain.profile.repository.ProfileRepository;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;
import teamficial.teamficial_be.domain.recruitingPost.repository.RecruitingPostRepository;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.domain.user.repository.UserRepository;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;
import teamficial.teamficial_be.global.apiPayload.exception.handler.NotFoundHandler;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final RecruitingPostRepository recruitingPostRepository;
    private final ApplicationRepository applicationRepository;

    public ApplicationDTO.ApplicationResponseDTO createApplication(Long userId, ApplicationDTO.ApplicationRequestDTO req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_USER));

        Profile profile = profileRepository.findById(req.getProfileId())
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_PROFILE));

        RecruitingPost recruitingPost = recruitingPostRepository.findById(req.getRecruitingPostId())
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_RECRUITING_POST));

        boolean exists = applicationRepository.existsByUserIdAndRecruitingPostId(userId, req.getRecruitingPostId()) == 1;


        if (exists) {
            throw new GeneralException(ErrorStatus.DUPLICATE_APPLICATION);
        }

        Application application = Application.builder()
                .user(user)
                .profile(profile)
                .recruitingPost(recruitingPost)
                .content(req.getContent())
                .applcationStatus(ApplicationStatus.WAITING)
                .build();

        Application saved = applicationRepository.save(application);

        return ApplicationDTO.ApplicationResponseDTO.builder()
                .applicationId(saved.getId())
                .status(saved.getApplcationStatus().name())
                .userId(saved.getUser().getId())
                .profileId(saved.getProfile().getId())
                .recruitingPostId(saved.getRecruitingPost().getId())
                .message("지원이 완료되었습니다.")
                .build();


    }
}
