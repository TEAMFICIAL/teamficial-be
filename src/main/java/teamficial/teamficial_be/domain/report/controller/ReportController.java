package teamficial.teamficial_be.domain.report.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "신고 관련 API")
@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "팀피셜록 신고하기 API", description = """
            유저가 자신에게 달린 팀피셜록을 신고하는 API입니다. \n
            ## Request Body
            `reportType` : HATE_SPEECH, UNSUITABLE_KEYWORD, OTHER 중 택 1 \n
                - `HATE_SPEECH` : 비방적인 내용입니다. \n
                - `UNSUITABLE_KEYWORD` : 적합하지 않은 내용의 키워드입니다. \n
                - `OTHER` : 기타 (직접 입력) \n
            `reportEtc` : reportType이 기타일 경우, 입력해주세요. \n
            `content` : 신고 내용
            """)
    @PostMapping("/reports/{keywordCommentId}")
    public ApiResponse<String> reportTeamficialLog(
            @AuthenticationPrincipal AuthDetails authDetails,
            @PathVariable Long keywordCommentId,
            @RequestBody @Valid ReportRequestDto reportRequestDto) {

        reportService.reportTeamficialLog(authDetails.user(), keywordCommentId, reportRequestDto);

        return ApiResponse.onSuccess("신고 내용이 팀피셜에게 전달되었습니다.");
    }

}
