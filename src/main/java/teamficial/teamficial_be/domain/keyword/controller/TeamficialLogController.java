package teamficial.teamficial_be.domain.keyword.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import teamficial.teamficial_be.domain.keyword.dto.response.HeadKeywordResponseDto;
import teamficial.teamficial_be.domain.keyword.service.TeamficialLogService;
import teamficial.teamficial_be.global.apiPayload.ApiResponse;
import teamficial.teamficial_be.global.security.AuthDetails;

@RestController
@RequiredArgsConstructor
public class TeamficialLogController {

    private final TeamficialLogService teamficialLogService;

    @GetMapping("/teamficial-log/head-keyword/{profileId}")
    @Operation(summary = "프로필의 대표 키워드 조회하기", description = "해당 프로필의 대표 키워드를 조회하는 api입니다.")
    public ApiResponse<HeadKeywordResponseDto> getHeadKeyword(@PathVariable("profileId") Long profileId, @AuthenticationPrincipal AuthDetails authDetails) {
        HeadKeywordResponseDto responseDto = teamficialLogService.getHeadKeyword(authDetails.user(),profileId);
        return ApiResponse.onSuccess(responseDto);
    }
}
