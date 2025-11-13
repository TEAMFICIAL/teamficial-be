package teamficial.teamficial_be.domain.profile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamficial.teamficial_be.domain.keyword.service.HeadKeywordService;
import teamficial.teamficial_be.domain.profile.dto.request.ProfileRequestDto;
import teamficial.teamficial_be.domain.profile.dto.response.ProfileResponseDto;
import teamficial.teamficial_be.domain.profile.entity.Profile;
import teamficial.teamficial_be.domain.profile.entity.ProfileLink;
import teamficial.teamficial_be.domain.profile.repository.ProfileRepository;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.domain.user.service.UserService;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final PreSignedUrlService preSignedUrlService;

    @Transactional
    public ProfileResponseDto createProfile(User user, ProfileRequestDto requestDto, String objectKey){
        String imageUrl=null;
        if (objectKey != null && !objectKey.isEmpty()) {
            imageUrl = preSignedUrlService.getPublicUrl(objectKey);
        }

        Profile profile = Profile.builder()
                .user(user)
                .userName(user.getName())
                .workingTime(requestDto.getWorkingTime())
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

        String image = profile.getProfileImage();
        if (image != null && !image.isBlank()) {
            String objectKey = preSignedUrlService.extractKeyFromUrl(image);
            preSignedUrlService.deleteByKey(objectKey);
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
        List<Profile> profiles = profileRepository.findAllByUser(user);
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
