package teamficial.teamficial_be.domain.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import teamficial.teamficial_be.global.redis.RedisService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final RecruitingPostRepository recruitingPostRepository;
    private final ApplicationRepository applicationRepository;
    private final RedisService redisService;

    @Transactional
    public ApplicationDTO.ApplicationResponseDTO createApplication(Long userId, ApplicationDTO.ApplicationRequestDTO req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_USER));

        Profile profile = profileRepository.findById(req.getProfileId())
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_PROFILE));

        if (!profile.getUser().getId().equals(userId)) {
            throw new GeneralException(ErrorStatus._FORBIDDEN);
        }

        RecruitingPost recruitingPost = recruitingPostRepository.findById(req.getRecruitingPostId())
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_RECRUITING_POST));

        boolean exists = applicationRepository.existsByUserIdAndRecruitingPostId(userId, req.getRecruitingPostId()) == 1;


        if (exists) {
            throw new GeneralException(ErrorStatus.DUPLICATE_APPLICATION);
        }


        Application application = Application.builder()
                .user(user)
                .profile(profile)
                .position(req.getPosition())
                .recruitingPost(recruitingPost)
                .content(req.getContent())
                .applicationStatus(ApplicationStatus.MATCHING)
                .build();

        Application saved = applicationRepository.save(application);
        applicantIncrement(recruitingPost);

        return ApplicationDTO.ApplicationResponseDTO.builder()
                .applicationId(saved.getId())
                .position(saved.getPosition())
                .status(saved.getApplicationStatus().name())
                .userId(saved.getUser().getId())
                .profileId(saved.getProfile().getId())
                .recruitingPostId(saved.getRecruitingPost().getId())
                .message("지원이 완료되었습니다.")
                .build();


    }

    public Application getApplication(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(()->new NotFoundHandler(ErrorStatus.NOT_FOUND_APPLICAION));
    }

    public List<Application> getApplicationsByRecruitingPost(RecruitingPost recruitingPost) {
        return applicationRepository.findAllByRecruitingPost(recruitingPost);
    }

    public Page<Application> getApplicationsByUserAndStatus(User user, Pageable pageable,ApplicationStatus status) {
        if (status == null){
            return applicationRepository.findAllByUser(user,pageable);
        } else {
            return applicationRepository.findAllByUserAndApplicationStatus(user,pageable,status);
        }
    }

    public void saveApplication(Application application) {
        applicationRepository.save(application);
    }

    public void saveApplications(List<Application> applications) {
        applicationRepository.saveAll(applications);
    }

    public List<Application> getTop3ByUser(User user) {
        return applicationRepository.findTop3ByUserOrderByCreatedAtDesc(user);
    }

    public int getApplicantCount(RecruitingPost recruitingPost) {
        return applicationRepository.countAllByRecruitingPost(recruitingPost);
    }

    @Transactional
    public void applicantIncrement(RecruitingPost recruitingPost) {
        String key = redisService.applicationKey(recruitingPost.getId());
        try {
            // 존재하는 경우 increment
            if (redisService.checkExistsValue(key)) {
                redisService.incrementValue(key, 1);
            } else {
                // 키 없으면 저장
                int count = applicationRepository.countAllByRecruitingPost(recruitingPost);
                redisService.setValue(key, String.valueOf(count + 1), 0L);
            }
        } catch (Exception e) {
            log.warn("Redis increment error key={}, postId={}", key, recruitingPost.getId(), e);
        }
    }
}
