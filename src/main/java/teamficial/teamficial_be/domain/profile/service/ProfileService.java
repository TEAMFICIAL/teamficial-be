package teamficial.teamficial_be.domain.profile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamficial.teamficial_be.domain.profile.dto.request.ProfileRequestDto;
import teamficial.teamficial_be.domain.profile.dto.response.ProfileResponseDto;
import teamficial.teamficial_be.domain.profile.entity.Profile;
import teamficial.teamficial_be.domain.profile.repository.ProfileRepository;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.domain.user.service.UserService;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final PreSignedUrlService preSignedUrlService;
    private final UserService userService;

    @Transactional
    public ProfileResponseDto createProfile(Long userId, ProfileRequestDto requestDto, String objectKey){
        User user = userService.getUserById(userId);
        String imageUrl = preSignedUrlService.getPublicUrl(objectKey);

        Profile profile = Profile.builder()
                .user(user)
                .userName(user.getName())
                .position(requestDto.getPosition())
                .workingTime(requestDto.getWorkingTime())
                .profileName(requestDto.getProfileName())
                .contactWay(requestDto.getContactWay())
                .link(requestDto.getLink())
                .profileImage(imageUrl)
                .build();

        profileRepository.save(profile);
        return ProfileResponseDto.of(profile);
    }

    @Transactional
    public ProfileResponseDto updateProfile(Long profileId, ProfileRequestDto requestDto){
        Profile profile = getProfileById(profileId);
        profile.update(requestDto);
        profileRepository.save(profile);
        return ProfileResponseDto.of(profile);
    }

    @Transactional(readOnly = true)
    public ProfileResponseDto getProfile(Long profileId){
        Profile profile = getProfileById(profileId);
        return ProfileResponseDto.of(profile);
    }

    @Transactional
    public void deleteProfile(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_FOUND_PROFILE));

        String objectKey = preSignedUrlService.extractKeyFromUrl(profile.getProfileImage());
        preSignedUrlService.deleteByKey(objectKey);

        profileRepository.delete(profile);
    }

    @Transactional
    public void deleteProfileImage(Long profileId){
        Profile profile = getProfileById(profileId);

        if(profile.getProfileImage() == null){
            throw new GeneralException(ErrorStatus.ALREADY_DELETED_PROFILE_IMAGE);
        }

        String objectKey = preSignedUrlService.extractKeyFromUrl(profile.getProfileImage());
        preSignedUrlService.deleteByKey(objectKey);
        profile.deleteProfileImage();
        profileRepository.save(profile);
    }

    @Transactional
    public String updateProfileImage(Long profileId, String objectKey){
        Profile profile = getProfileById(profileId);
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
}
