package teamficial.teamficial_be.domain.profile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamficial.teamficial_be.domain.application.entity.Application;
import teamficial.teamficial_be.domain.application.entity.ApplicationStatus;
import teamficial.teamficial_be.domain.application.service.ApplicationService;
import teamficial.teamficial_be.domain.keyword.service.HeadKeywordService;
import teamficial.teamficial_be.domain.profile.dto.request.ProfileRequestDto;
import teamficial.teamficial_be.domain.profile.dto.response.ProfileResponseDto;
import teamficial.teamficial_be.domain.profile.entity.Profile;
import teamficial.teamficial_be.domain.profile.entity.ProfileLink;
import teamficial.teamficial_be.domain.profile.entity.WorkingTime;
import teamficial.teamficial_be.domain.profile.repository.ProfileRepository;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingStatus;
import teamficial.teamficial_be.domain.recruitingPost.service.RecruitingPostService;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.domain.user.service.UserService;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ApplicationService applicationService;
    private final ProfileRepository profileRepository;
    private final PreSignedUrlService preSignedUrlService;
    private final RecruitingPostService recruitingPostService;

    @Transactional
    public ProfileResponseDto createProfile(User user, ProfileRequestDto requestDto, String objectKey){
        String imageUrl=null;
        if (objectKey != null && !objectKey.isEmpty()) {
            imageUrl = preSignedUrlService.getPublicUrl(objectKey);
        }

        WorkingTime workingTime = null;
        if (requestDto.getWorkingTime() != null) {
            workingTime = requestDto.getWorkingTime();
        }

        int count = profileRepository.countByUser(user);

        if (count > 3) {
            throw new GeneralException(ErrorStatus.CANNOT_COUNT_OVER_3);
        }

        Profile profile = Profile.builder()
                .user(user)
                .userName(user.getName())
                .workingTime(workingTime)
                .profileName(requestDto.getProfileName())
                .contactWay(requestDto.getContactWay())
                .profileImage(imageUrl)
                .build();

        if (requestDto.getLinks() != null) {
            requestDto.getLinks().forEach(link -> profile.addLink(link));
        }

        profileRepository.save(profile);
        return ProfileResponseDto.of(profile,profile.getHeadKeywords());
    }

    @Transactional
    public ProfileResponseDto updateProfile(User user,Long profileId, ProfileRequestDto requestDto){
        Profile profile = getProfileById(profileId);
        profile.update(requestDto);

        validateUserProfile(user, profile);

        if (requestDto.getLinks() != null) {
            profile.clearLinks();
            requestDto.getLinks().forEach(link -> profile.addLink(link));
        }

        profileRepository.save(profile);
        return ProfileResponseDto.of(profile,profile.getHeadKeywords());
    }

    @Transactional(readOnly = true)
    public ProfileResponseDto getProfile(Long profileId){
        Profile profile = getProfileById(profileId);
        return ProfileResponseDto.of(profile,profile.getHeadKeywords());
    }

    @Transactional
    public void deleteProfile(User user,Long profileId) {

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_FOUND_PROFILE));

        validateUserProfile(user, profile);

        // 이 프로필을 사용한 Application 존재하는지 확인
        List<Application> applications = applicationService.getApplicationsByProfile(profile);
        List<RecruitingPost> recruitingPosts  = recruitingPostService.getRecruitingPostsByProfile(profile);
        if (!applications.isEmpty() || !recruitingPosts.isEmpty()) {
            // 존재한다면, applicationStatus 확인
            boolean applicationHasMatchable = applications.stream()
                    .anyMatch(app -> app.getApplicationStatus() == ApplicationStatus.MATCHED
                            || app.getApplicationStatus() == ApplicationStatus.MATCH_FAILED);

            boolean recruitingPostHasMatchable = recruitingPosts.stream()
                    .anyMatch(recruitingPost -> recruitingPost.getStatus()== RecruitingStatus.CLOSED);

            if (applicationHasMatchable && recruitingPostHasMatchable) {
                // soft-delete 처리
                profile.deleteProfile();
                profileRepository.save(profile);
            } else if (!applicationHasMatchable) {
                // 매칭 대기 상태라면 삭제 금지
                throw new GeneralException(ErrorStatus.CANNOT_DELETE_PROFILE_WHEN_APPLICATION_PENDING);
            } else if (!recruitingPostHasMatchable) {
                throw new GeneralException(ErrorStatus.CANNOT_DELETE_PROFILE_WHEN_RECRUITINGPOST_OPEN);
            }
        } else {
            // 3) Application이 아예 없다면 물리 삭제 가능
            // 이미지 삭제 로직 수행
            String image = profile.getProfileImage();
            if (image != null && !image.isBlank()) {
                String objectKey = preSignedUrlService.extractKeyFromUrl(image);
                preSignedUrlService.deleteByKey(objectKey);
            }
            profileRepository.delete(profile);
        }

        //
    }

    @Transactional
    public void deleteProfileImage(User user,Long profileId){
        Profile profile = getProfileById(profileId);

        validateUserProfile(user, profile);

        if(profile.getProfileImage() == null){
            throw new GeneralException(ErrorStatus.ALREADY_DELETED_PROFILE_IMAGE);
        }

        String objectKey = preSignedUrlService.extractKeyFromUrl(profile.getProfileImage());
        preSignedUrlService.deleteByKey(objectKey);
        profile.deleteProfileImage();
        profileRepository.save(profile);
    }

    @Transactional
    public String updateProfileImage(User user,Long profileId, String objectKey){
        Profile profile = getProfileById(profileId);

        validateUserProfile(user, profile);

        if (profile.getProfileImage() != null) {
            String oldObjectKey = preSignedUrlService.extractKeyFromUrl(profile.getProfileImage());
            preSignedUrlService.deleteByKey(oldObjectKey);
        }
        String imageUrl = preSignedUrlService.getPublicUrl(objectKey);
        profile.updateProfileImage(imageUrl);
        profileRepository.save(profile);
        return imageUrl;
    }

    public Profile getProfileById(Long profileId){
        return profileRepository.findById(profileId)
                .orElseThrow(()-> new GeneralException(ErrorStatus.NOT_FOUND_PROFILE));
    }

    @Transactional(readOnly = true)
    public List<ProfileResponseDto> getProfileList(User user) {
        List<Profile> profiles = profileRepository.findAllByUserAndIsDeletedFalse(user);
        return profiles.stream()
                .map(profile -> ProfileResponseDto.of(profile,profile.getHeadKeywords()))
                .toList();
    }

    private void validateUserProfile(User user,Profile profile) {
        if(!user.getId().equals(profile.getUser().getId())) {
            throw new GeneralException(ErrorStatus._FORBIDDEN);
        }
    }

    public void saveProfile(Profile profile) {
        profileRepository.save(profile);
    }
}
