package teamficial.teamficial_be.domain.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamficial.teamficial_be.domain.application.dto.applicationDTO;
import teamficial.teamficial_be.domain.profile.entity.Profile;
import teamficial.teamficial_be.domain.profile.repository.ProfileRepository;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.domain.user.repository.UserRepository;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public applicationDTO.ApplicationResponseDTO createApplication(Long userId, applicationDTO.ApplicationRequestDTO req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_FOUND_USER));

        Profile profile = profileRepository.findById(req.getProfileId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.PROFILE_NOT_FOUND));

        // 3️⃣ RecruitingPost 조회
        RecruitingPost recruitingPost = recruitingPostRepository.findById(req.getRecruitingPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.RECRUITING_POST_NOT_FOUND));


    }
}
