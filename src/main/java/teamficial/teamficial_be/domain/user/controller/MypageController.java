package teamficial.teamficial_be.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import teamficial.teamficial_be.domain.application.dto.response.ApplicationResponseDto;
import teamficial.teamficial_be.domain.user.dto.CurrentApplicationResponseDto;
import teamficial.teamficial_be.domain.user.service.MypageService;
import teamficial.teamficial_be.global.apiPayload.ApiResponse;
import teamficial.teamficial_be.global.enums.Position;
import teamficial.teamficial_be.global.security.AuthDetails;

@RestController
@RequiredArgsConstructor
@Tag(name = "Mypage 관련 API", description = "피그마 GUI 4.2, 4.3")
public class MypageController {

    private final MypageService mypageService;

    @GetMapping("/my-page/{recruitingPostId}/current-application")
    @Operation(summary = "지원자 현황 조회", description = "마이페이지에서 지원자 현황을 조회하는 API입니다.")
    public ApiResponse<CurrentApplicationResponseDto> getCurrentApplication(@PathVariable Long recruitingPostId,
                                                                            @AuthenticationPrincipal AuthDetails authDetails,
                                                                            @RequestParam(required = false) Position position) {
        CurrentApplicationResponseDto responseDto = mypageService.getCurrentApplication(recruitingPostId,authDetails.user(),position);
        return ApiResponse.onSuccess(responseDto);
    }

    @PatchMapping("/my-page/{recruitingPostId}")
    @Operation(summary = "팀원 모집 마감", description = "모집 글에서 팀원 모집을 마감하는 API입니다.")
    public ApiResponse<String> closedApplication(@AuthenticationPrincipal AuthDetails authDetails, @PathVariable Long recruitingPostId){
        mypageService.closedApplication(authDetails.user(),recruitingPostId);
        return ApiResponse.onSuccess("팀원 모집을 마감하였습니다.");
    }

    @GetMapping("my-page/{recruitingPostId}/{applicationId}")
    @Operation(summary = "지원자 프로필 조회", description = "지원자의 프로필, 지원 글을 조회하는 API입니다.")
    public ApiResponse<ApplicationResponseDto> getApplicantProfile(@AuthenticationPrincipal AuthDetails authDetails, @PathVariable Long recruitingPostId, @PathVariable Long applicationId){
        ApplicationResponseDto applicationResponseDto = mypageService.getApplicantProfile(authDetails.user(),recruitingPostId,applicationId);

        return ApiResponse.onSuccess(applicationResponseDto);
    }

    @PatchMapping("my-page/{recruitingPostId}/{applicationId}")
    @Operation(summary = "지원자와 함께하기", description = "지원자를 합격시키는 API입니다.")
    public ApiResponse<String> confirmedApplicant(@AuthenticationPrincipal AuthDetails authDetails, @PathVariable Long recruitingPostId, @PathVariable Long applicationId){
        mypageService.confirmedApplicant(authDetails.user(),recruitingPostId,applicationId);

        return ApiResponse.onSuccess("함께할 사람이 생겼습니다!");
    }
}
