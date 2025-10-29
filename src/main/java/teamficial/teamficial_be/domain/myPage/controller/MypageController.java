package teamficial.teamficial_be.domain.myPage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import teamficial.teamficial_be.domain.application.dto.response.ApplicationResponseDto;
import teamficial.teamficial_be.domain.myPage.dto.response.CurrentApplicantResponseDto;
import teamficial.teamficial_be.domain.myPage.dto.response.CurrentApplicationDetailResponseDto;
import teamficial.teamficial_be.domain.myPage.dto.response.MyApplicationResponseDto;
import teamficial.teamficial_be.domain.myPage.service.MypageService;
import teamficial.teamficial_be.global.apiPayload.ApiResponse;
import teamficial.teamficial_be.global.enums.Position;
import teamficial.teamficial_be.global.security.AuthDetails;
import teamficial.teamficial_be.global.util.PagedResponse;

@RestController
@RequiredArgsConstructor
@Tag(name = "Mypage 관련 API", description = "피그마 GUI 4.2, 4.3")
public class MypageController {

    private final MypageService mypageService;

    @GetMapping("/my-page/applications")
    @Operation(summary = "내가 지원한 팀 리스트 조회하기", description = "마이페이지에서 내가 지원한 팀들을 조회하는 API입니다.")
    public ApiResponse<PagedResponse<MyApplicationResponseDto>> getMyPageApplications(@AuthenticationPrincipal AuthDetails authDetails,
                                                                                      @RequestParam(defaultValue = "0") int page,
                                                                                      @RequestParam(defaultValue = "3") int size) {
        PagedResponse<MyApplicationResponseDto> responseDtos = mypageService.getAllApplications(authDetails.user(),page,size);
        return ApiResponse.onSuccess(responseDtos);
    }



    @GetMapping("/my-page/{recruitingPostId}/current-applicants")
    @Operation(summary = "지원자 현황 조회", description = "마이페이지에서 지원자 현황을 조회하는 API입니다.")
    public ApiResponse<CurrentApplicationDetailResponseDto> getCurrentApplication(@PathVariable Long recruitingPostId,
                                                                                  @AuthenticationPrincipal AuthDetails authDetails,
                                                                                  @RequestParam(required = false) Position position) {
        CurrentApplicationDetailResponseDto responseDto = mypageService.getCurrentApplication(recruitingPostId,authDetails.user(),position);
        return ApiResponse.onSuccess(responseDto);
    }

    @PatchMapping("/my-page/{recruitingPostId}")
    @Operation(summary = "팀원 모집 마감", description = "모집 글에서 팀원 모집을 마감하는 API입니다.")
    public ApiResponse<String> closedApplication(@AuthenticationPrincipal AuthDetails authDetails, @PathVariable Long recruitingPostId){
        mypageService.closedApplication(authDetails.user(),recruitingPostId);
        return ApiResponse.onSuccess("팀원 모집을 마감하였습니다.");
    }

    @GetMapping("/my-page/{recruitingPostId}/{applicationId}")
    @Operation(summary = "지원자 프로필 조회", description = "지원자의 프로필, 지원 글을 조회하는 API입니다.")
    public ApiResponse<ApplicationResponseDto> getApplicantProfile(@AuthenticationPrincipal AuthDetails authDetails, @PathVariable Long recruitingPostId, @PathVariable Long applicationId){
        ApplicationResponseDto applicationResponseDto = mypageService.getApplicantProfile(authDetails.user(),recruitingPostId,applicationId);

        return ApiResponse.onSuccess(applicationResponseDto);
    }

    @PatchMapping("/my-page/{recruitingPostId}/{applicationId}")
    @Operation(summary = "지원자와 함께하기", description = "지원자를 합격시키는 API입니다.")
    public ApiResponse<String> confirmedApplicant(@AuthenticationPrincipal AuthDetails authDetails, @PathVariable Long recruitingPostId, @PathVariable Long applicationId){
        mypageService.confirmedApplicant(authDetails.user(),recruitingPostId,applicationId);

        return ApiResponse.onSuccess("함께할 사람이 생겼습니다!");
    }
}
