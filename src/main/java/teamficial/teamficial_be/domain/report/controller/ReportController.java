package teamficial.teamficial_be.domain.report.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import teamficial.teamficial_be.domain.report.dto.ReportRequestDto;
import teamficial.teamficial_be.domain.report.service.ReportService;
import teamficial.teamficial_be.global.apiPayload.ApiResponse;
import teamficial.teamficial_be.global.security.AuthDetails;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "팀피셜록 신고하기 API", description = "유저가 자신에게 달린 팀피셜록을 신고하는 API입니다.")
    @PostMapping("/reports/{keywordCommentId}")
    public ApiResponse<String> reportTeamficialLog(
            @AuthenticationPrincipal AuthDetails authDetails,
            @PathVariable Long keywordCommentId,
            @RequestBody @Valid ReportRequestDto reportRequestDto) {

        reportService.reportTeamficialLog(authDetails.user(), keywordCommentId, reportRequestDto);

        return ApiResponse.onSuccess("신고 내용이 팀피셜에게 전달되었습니다.");
    }

}
