package teamficial.teamficial_be.domain.keyword.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import teamficial.teamficial_be.domain.keyword.dto.request.HeadKeywordRequestDto;
import teamficial.teamficial_be.domain.keyword.dto.response.HeadKeywordResponseDto;
import teamficial.teamficial_be.domain.keyword.dto.response.KeywordListResponseDto;
import teamficial.teamficial_be.domain.keyword.service.TeamficialLogService;
import teamficial.teamficial_be.global.apiPayload.ApiResponse;
import teamficial.teamficial_be.global.security.AuthDetails;
import teamficial.teamficial_be.global.util.PagedResponse;

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

    @PutMapping("/teamficial-log/head-keyword/{profileId}")
    @Operation(summary = "대표 키워드 선택하기", description = "해당 프로필의 대표 키워드를 선택하는 api입니다.")
    public ApiResponse<HeadKeywordResponseDto> updateHeadKeyword(@PathVariable("profileId") Long profileId, HeadKeywordRequestDto requestDto, @AuthenticationPrincipal AuthDetails authDetails) {
        HeadKeywordResponseDto responseDto = teamficialLogService.updateHeadKeyword(authDetails.user(),profileId,requestDto);

        return ApiResponse.onSuccess(responseDto);
    }

    @GetMapping("/teamficial-log/{userId}")
    @Operation(summary = "키워드 리스트 조회하기", description = "한 유저의 키워드 리스트를 조회하는 api 입니다.")
    public ApiResponse<PagedResponse<KeywordListResponseDto>> getKeywordList(@PathVariable Long userId,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "3") int size) {
        PagedResponse<KeywordListResponseDto> responseDto = teamficialLogService.getKeywordList(userId,page,size);

        return ApiResponse.onSuccess(responseDto);
    }
}
