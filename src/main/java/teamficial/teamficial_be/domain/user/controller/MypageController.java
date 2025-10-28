package teamficial.teamficial_be.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
}
