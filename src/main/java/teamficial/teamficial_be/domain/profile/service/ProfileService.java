package teamficial.teamficial_be.domain.profile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamficial.teamficial_be.domain.application.entity.Application;
import teamficial.teamficial_be.domain.application.entity.ApplicationStatus;
import teamficial.teamficial_be.domain.application.service.ApplicationService;
import teamficial.teamficial_be.domain.profile.dto.ProfileStatus;
import teamficial.teamficial_be.domain.profile.dto.request.ProfileRequestDto;
import teamficial.teamficial_be.domain.profile.dto.response.ProfileResponseDto;
import teamficial.teamficial_be.domain.profile.entity.Profile;
import teamficial.teamficial_be.domain.profile.entity.WorkingTime;
import teamficial.teamficial_be.domain.profile.repository.ProfileRepository;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingStatus;
import teamficial.teamficial_be.domain.recruitingPost.service.RecruitingPostService;
import teamficial.teamficial_be.domain.user.entity.User;
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

        if (count >= 3) {
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
    public ProfileResponseDto updateProfile(User user,Long profileId, ProfileRequestDto requestDto) {
        Profile profile = getProfileById(profileId);

        validateUserProfile(user, profile);

        ProfileStatus status = checkProfileStatus(profile);

        if (!status.canModifyOrDelete()){
            status.validateForModification();
            throw new GeneralException(ErrorStatus.CANNOT_MODIFY_PROFILE);
        }

        profile.update(requestDto);

        if (requestDto.getLinks() != null) {
            profile.clearLinks();
            requestDto.getLinks().forEach(profile::addLink);
        }

        profileRepository.save(profile);
        return ProfileResponseDto.of(profile, profile.getHeadKeywords());
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

        ProfileStatus status = checkProfileStatus(profile);

        if (status.canHardDelete()){
            String image = profile.getProfileImage();
            if (image != null && !image.isBlank()) {
                String objectKey = preSignedUrlService.extractKeyFromUrl(image);
                preSignedUrlService.deleteByKey(objectKey);
            }
            profileRepository.delete(profile);
            return;
        }

        if (!status.canModifyOrDelete()){
            status.validateForDeletion();
            throw new GeneralException(ErrorStatus.CANNOT_DELETE_PROFILE);
        }

        profile.deleteProfile();
        profileRepository.save(profile);
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

    private ProfileStatus checkProfileStatus(Profile profile){
        //이 프로필의 application 또는 작성한 recruitingPost 있는지
        List<Application> applications = applicationService.getApplicationsByProfile(profile);
        List<RecruitingPost> recruitingPosts = recruitingPostService.getRecruitingPostsByProfile(profile);

        boolean hasAnyApplication = !applications.isEmpty();
        boolean hasMatchingApplication = applications.stream()
                .anyMatch(a -> a.getApplicationStatus() == ApplicationStatus.MATCHING || a.getApplicationStatus() == ApplicationStatus.TEMP_SAVED);

        boolean hasAnyPost = !recruitingPosts.isEmpty();
        boolean hasOpenPost = recruitingPosts.stream()
                .anyMatch(p -> p.getStatus() == RecruitingStatus.OPEN);

        return ProfileStatus.builder()
                .hasMatchingApplication(hasMatchingApplication)
                .hasAnyApplication(hasAnyApplication)
                .hasAnyPost(hasAnyPost)
                .hasOpenPost(hasOpenPost)
                .build();
    }
}
