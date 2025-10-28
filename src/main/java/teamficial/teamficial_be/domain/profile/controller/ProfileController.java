package teamficial.teamficial_be.domain.profile.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import teamficial.teamficial_be.domain.profile.dto.request.ProfileRequestDto;
import teamficial.teamficial_be.domain.profile.dto.response.ProfileResponseDto;
import teamficial.teamficial_be.domain.profile.service.ProfileService;
import teamficial.teamficial_be.global.apiPayload.ApiResponse;
import teamficial.teamficial_be.global.security.AuthDetails;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping("/profile")
    @Operation(summary = "프로필 생성", description = "프로필을 생성하는 API입니다.")
    public ApiResponse<ProfileResponseDto> createProfile(@AuthenticationPrincipal AuthDetails authDetails,
                                                         @RequestBody ProfileRequestDto requestDto,
                                                         @Nullable @RequestParam String objectKey) {
        ProfileResponseDto profileResponseDto = profileService.createProfile(authDetails.user(), requestDto, objectKey);
        return ApiResponse.onSuccess(profileResponseDto);
    }

    @GetMapping("/profile")
    @Operation(summary = "프로필리스트 조회", description = "프로필을 조회하는 API입니다.")
    public ApiResponse<List<ProfileResponseDto>> getProfileList(@AuthenticationPrincipal AuthDetails authDetails) {
        List<ProfileResponseDto> profileResponseDtos = profileService.getProfileList(authDetails.user());
        return ApiResponse.onSuccess(profileResponseDtos);
    }

    @GetMapping("/profile/{profileId}")
    @Operation(summary = "프로필 상세 조회", description = "프로필을 조회하는 API입니다.")
    public ApiResponse<ProfileResponseDto> getProfile(@PathVariable Long profileId) {
        ProfileResponseDto profileResponseDto = profileService.getProfile(profileId);
        return ApiResponse.onSuccess(profileResponseDto);
    }

    @PutMapping("/profile/{profileId}")
    @Operation(summary = "프로필 수정", description = "프로필을 수정하는 API입니다.")
    public ApiResponse<ProfileResponseDto> updateProfile(@PathVariable Long profileId,
                                                         @RequestBody ProfileRequestDto profileRequestDto){
        ProfileResponseDto profileResponseDto = profileService.updateProfile(profileId, profileRequestDto);
        return ApiResponse.onSuccess(profileResponseDto);
    }

    @PutMapping("profile/{profileId}/image")
    @Operation(summary = "프로필 사진 수정", description = "프로필 사진을 수정하는 API입니다.")
    public ApiResponse<String> updateProfileImage(@PathVariable Long profileId, @RequestParam String objectKey) {
        String newProfileImage = profileService.updateProfileImage(profileId, objectKey);
        return ApiResponse.onSuccess(newProfileImage);
    }

    @DeleteMapping("profile/{profileId}")
    @Operation(summary = "프로필 삭제", description = "프로필을 삭제하는 API입니다.")
    public ApiResponse<String> deleteProfile(@PathVariable Long profileId) {
        profileService.deleteProfile(profileId);
        return ApiResponse.onSuccess("프로필이 삭제되었습니다.");
    }

    @DeleteMapping("/profile/{profileId}/image")
    @Operation(summary = "프로필 사진 삭제", description = "프로필 사진을 삭제하는 API입니다.")
    public ApiResponse<String> deleteProfileImage(@PathVariable Long profileId) {
        profileService.deleteProfileImage(profileId);
        return ApiResponse.onSuccess("프로필 사진이 삭제되었습니다.");
    }
}
