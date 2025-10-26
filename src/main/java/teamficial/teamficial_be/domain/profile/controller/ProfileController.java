package teamficial.teamficial_be.domain.profile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import teamficial.teamficial_be.domain.profile.dto.request.ProfileRequestDto;
import teamficial.teamficial_be.domain.profile.dto.response.ProfileResponseDto;
import teamficial.teamficial_be.domain.profile.service.ProfileService;
import teamficial.teamficial_be.global.apiPayload.ApiResponse;

@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping("/profile")
    public ApiResponse<ProfileResponseDto> createProfile(@RequestParam Long userId,
                                                         @RequestBody ProfileRequestDto requestDto,
                                                         @RequestParam String objectKey) {
        ProfileResponseDto profileResponseDto = profileService.createProfile(userId, requestDto, objectKey);
        return ApiResponse.onSuccess(profileResponseDto);
    }

    @GetMapping("/profile/{profileId}")
    public ApiResponse<ProfileResponseDto> getProfile(@PathVariable Long profileId) {
        ProfileResponseDto profileResponseDto = profileService.getProfile(profileId);
        return ApiResponse.onSuccess(profileResponseDto);
    }

    @PutMapping("/profile/{profileId}")
    public ApiResponse<ProfileResponseDto> updateProfile(@PathVariable Long profileId,
                                                         @RequestBody ProfileRequestDto profileRequestDto){
        ProfileResponseDto profileResponseDto = profileService.updateProfile(profileId, profileRequestDto);
        return ApiResponse.onSuccess(profileResponseDto);
    }

    @PutMapping("profile/{profileId}/image")
    public ApiResponse<String> updateProfileImage(@PathVariable Long profileId, @RequestParam String objectKey) {
        String newProfileImage = profileService.updateProfileImage(profileId, objectKey);
        return ApiResponse.onSuccess(newProfileImage);
    }


}
