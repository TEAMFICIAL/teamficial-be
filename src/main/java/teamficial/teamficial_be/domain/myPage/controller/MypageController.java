package teamficial.teamficial_be.domain.myPage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import teamficial.teamficial_be.domain.application.dto.response.ApplicationResponseDto;
import teamficial.teamficial_be.domain.application.entity.ApplicationStatus;
import teamficial.teamficial_be.domain.myPage.dto.response.CurrentApplicantResponseDto;
import teamficial.teamficial_be.domain.myPage.dto.response.CurrentApplicationDetailResponseDto;
import teamficial.teamficial_be.domain.myPage.dto.response.DashboardResponseDto;
import teamficial.teamficial_be.domain.myPage.dto.response.MyApplicationResponseDto;
import teamficial.teamficial_be.domain.myPage.service.MypageService;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingStatus;
import teamficial.teamficial_be.global.apiPayload.ApiResponse;
import teamficial.teamficial_be.global.enums.Position;
import teamficial.teamficial_be.global.security.AuthDetails;
import teamficial.teamficial_be.global.util.PagedResponse;

@RestController
@RequiredArgsConstructor
@Tag(name = "Mypage 관련 API", description = "피그마 GUI 4.2, 4.3")
public class MypageController {

    private final MypageService mypageService;

    @GetMapping("/my-page/dashboard")
    @Operation(summary = "마이페이지 대쉬보드 조회", description = "GUI 4.1에 해당하는 내가 지원한 팀 3개, 작성한 모집 글 3개를 불러오는 API입니다.")
    public ApiResponse<DashboardResponseDto> getDashboard(@AuthenticationPrincipal AuthDetails authDetails) {
        DashboardResponseDto responseDto = mypageService.getUserDashBoard(authDetails.user());
        return ApiResponse.onSuccess(responseDto);
    }

    @GetMapping("/my-page/applications")
    @Operation(summary = "내가 지원한 팀 리스트 조회하기", description = "마이페이지에서 내가 지원한 팀들을 조회하는 API입니다.")
    public ApiResponse<PagedResponse<MyApplicationResponseDto>> getMyPageApplications(@AuthenticationPrincipal AuthDetails authDetails,
                                                                                      @RequestParam(required = false)ApplicationStatus applicationStatus,
                                                                                      @RequestParam(defaultValue = "0") int page,
                                                                                      @RequestParam(defaultValue = "3") int size) {
        PagedResponse<MyApplicationResponseDto> responseDtos = mypageService.getAllApplications(authDetails.user(),page,size,applicationStatus);
        return ApiResponse.onSuccess(responseDtos);
    }

    @GetMapping("/my-page/current-applicants")
    @Operation(summary = "작성한 모집 글들의 지원자 현황 조회하기", description = "마이페이지에서 작성한 모집 글들의 지원자 현황 조회하는 API입니다.")
    public ApiResponse<PagedResponse<CurrentApplicantResponseDto>> getAllCurrentApplication(@AuthenticationPrincipal AuthDetails authDetails,
                                                                                            @RequestParam(required = false) RecruitingStatus recruitingStatus,
                                                                                            @RequestParam(defaultValue = "0") int page,
                                                                                            @RequestParam(defaultValue = "3") int size) {
        PagedResponse<CurrentApplicantResponseDto> responseDtos = mypageService.getAllCurrentApplication(authDetails.user(),page,size, recruitingStatus);
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
    @Operation(summary = "지원자와 합불 정하기", description = "지원자를 합불을 정하는 API입니다.")
    public ApiResponse<String> confirmedApplicant(@AuthenticationPrincipal AuthDetails authDetails, @PathVariable Long recruitingPostId, @PathVariable Long applicationId, @RequestParam ApplicationStatus applicationStatus){
        mypageService.confirmedApplicant(authDetails.user(),recruitingPostId,applicationId,applicationStatus);

        return ApiResponse.onSuccess("지원 결과를 수정하였습니다.");
    }
}
