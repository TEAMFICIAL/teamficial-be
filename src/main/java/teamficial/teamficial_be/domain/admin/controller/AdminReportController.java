package teamficial.teamficial_be.domain.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import teamficial.teamficial_be.domain.admin.dto.ReportResponseDto;
import teamficial.teamficial_be.domain.admin.service.AdminReportService;
import teamficial.teamficial_be.global.apiPayload.ApiResponse;
import teamficial.teamficial_be.global.security.AuthDetails;
import teamficial.teamficial_be.global.util.ScrollResponse;

@Tag(name = "어드민 신고 관리 관련 API")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminReportController {

    private final AdminReportService adminReportService;

    @GetMapping("/report/{reportId}")
    @Operation(summary = "신고된 키워드 코멘트 조회하기", description = "어드민이 신고된 키워드를 조회할 때 사용하는 API입니다.")
    public ApiResponse<ReportResponseDto> getReport(@AuthenticationPrincipal AuthDetails authDetails, @PathVariable Long reportId){

        ReportResponseDto responseDto = adminReportService.getReport(authDetails.user(), reportId);

        return ApiResponse.onSuccess(responseDto);
    }

    @GetMapping("/report")
    @Operation(summary = "신고된 키워드 코멘트 리스트 조회하기", description = "어드민이 신고된 키워드를 조회할 때 사용하는 API입니다.")
    public ApiResponse<ScrollResponse<ReportResponseDto>> getReportList(@AuthenticationPrincipal AuthDetails authDetails,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size){

        ScrollResponse<ReportResponseDto> responseDtos = adminReportService.getReportList(authDetails.user(), page, size);

        return ApiResponse.onSuccess(responseDtos);
    }

    @PostMapping("/report/{reportId}")
    @Operation(summary = "신고된 키워드 코멘트 삭제하기", description = "어드민이 신고된 키워드를 삭제할 때 사용하는 API입니다.")
    public ApiResponse<String> acceptReport(@AuthenticationPrincipal AuthDetails authDetails, @PathVariable Long reportId){

        adminReportService.acceptReport(reportId);

        return ApiResponse.onSuccess("신고가 반영되었습니다.");
    }
}
