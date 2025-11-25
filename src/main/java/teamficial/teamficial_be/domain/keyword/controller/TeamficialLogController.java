package teamficial.teamficial_be.domain.keyword.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import teamficial.teamficial_be.domain.keyword.dto.request.HeadKeywordRequestDto;
import teamficial.teamficial_be.domain.keyword.dto.request.TeamficialLogRequestDto;
import teamficial.teamficial_be.domain.keyword.dto.response.*;
import teamficial.teamficial_be.domain.keyword.service.TeamficialLogService;
import teamficial.teamficial_be.global.apiPayload.ApiResponse;
import teamficial.teamficial_be.global.security.AuthDetails;
import teamficial.teamficial_be.global.util.PagedResponse;
import teamficial.teamficial_be.global.util.ScrollResponse;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Tag(name = "팀피셜록 관련 API", description = "기능명세서 3")
public class TeamficialLogController {

    private final TeamficialLogService teamficialLogService;

    @GetMapping("/teamficial-log/head-keyword/{profileId}")
    @Operation(summary = "프로필의 대표 키워드 조회하기", description = "해당 프로필의 대표 키워드를 조회하는 api입니다.")
    public ApiResponse<HeadKeywordResponseDto> getHeadKeyword(@PathVariable("profileId") Long profileId) {
        HeadKeywordResponseDto responseDto = teamficialLogService.getHeadKeyword(profileId);
        return ApiResponse.onSuccess(responseDto);
    }

    @PutMapping("/teamficial-log/head-keyword/{profileId}")
    @Operation(summary = "대표 키워드 등록/수정하기", description = "해당 프로필의 대표 키워드를 등록/수정하는 api입니다.")
    public ApiResponse<CreateHeadKeywordResponseDto> updateHeadKeyword(@PathVariable("profileId") Long profileId, @RequestParam(required = false) Long oldHeadKeywordId, @RequestParam Long keywordId, @AuthenticationPrincipal AuthDetails authDetails) {
        CreateHeadKeywordResponseDto responseDto = teamficialLogService.updateHeadKeyword(authDetails.user(),profileId,oldHeadKeywordId,keywordId);

        return ApiResponse.onSuccess(responseDto);
    }

    @GetMapping("/teamficial-log/{userId}")
    @Operation(summary = "키워드 리스트 조회하기", description = "한 유저의 키워드 리스트를 조회하는 api 입니다.")
    public ApiResponse<PagedResponse<KeywordResponseDto>> getKeywordList(@AuthenticationPrincipal AuthDetails authDetails,
                                                                         @PathVariable Long userId,
                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "3") int size) {
        PagedResponse<KeywordResponseDto> responseDto = teamficialLogService.getKeywordList(authDetails.user(),userId,page,size);

        return ApiResponse.onSuccess(responseDto);
    }

    @GetMapping("/teamficial-log/users/{keywordId}")
    @Operation(summary = "키워드 코멘트 리스트 조회하기", description = "키워드 코멘트 리스트를 조회하는 api 입니다.")
    public ApiResponse<ScrollResponse<KeywordCommentResponseDto>> getKeywordCommentList(@AuthenticationPrincipal AuthDetails authDetails,
                                                                                        @PathVariable Long keywordId,
                                                                                 @RequestParam(defaultValue = "0") int page,
                                                                                 @RequestParam(defaultValue = "3") int size) {
        ScrollResponse<KeywordCommentResponseDto> responseDto = teamficialLogService.getKeywordCommentList(authDetails.user(),keywordId,page,size);

        return ApiResponse.onSuccess(responseDto);
    }

    @PostMapping("/teamficial-log")
    @Operation(summary = "팀피셜록 작성하기", description = "팀피셜록을 작성하고 키워드, 코멘트 원문을 추출하는 api 입니다.")
    public ApiResponse<TeamficialLogResponseDto> createTeamficialLog(
            @Valid @RequestBody TeamficialLogRequestDto request,
            @AuthenticationPrincipal AuthDetails authDetails) throws IOException {

        return ApiResponse.onSuccess(teamficialLogService.createTeamficialLog(authDetails.user(), request));
    }

    @GetMapping("/teamficial-log/requester")
    @Operation(summary = "팀피셜록 요청자의 정보 반환 api")
    public ApiResponse<TeamficialLogRequesterResponseDto> getTeamficialLogRequester(@RequestParam String requesterUuid) {
        TeamficialLogRequesterResponseDto responseDto = teamficialLogService.getTeamficialLogRequester(requesterUuid);

        return ApiResponse.onSuccess(responseDto);
    }

    @GetMapping("/teamficial-log/rand")
    @Operation(summary = "팀피셜록 대표 키워드 중 3개 조회", description = "팀피셜록 대표 키워드 중 3개를 임의로 조회하는 API입니다.")
    public ApiResponse<KeywordRandResponseDto> getTeamficialLogRand3(
            @RequestParam String requesterUuid) {

        KeywordRandResponseDto responseDto =
                teamficialLogService.getTeamficialLogRand3(requesterUuid);

        return ApiResponse.onSuccess(responseDto);
    }


}
