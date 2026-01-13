package teamficial.teamficial_be.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import teamficial.teamficial_be.domain.report.entity.Report;

@Getter
@Builder
public class ReportResponseDto {
    @Schema(name= "신고 id")
    Long reportId;
    @Schema(name = "신고된 키워드 코멘트 id")
    Long commentId;
    @Schema(example = "신고 유형")
    String reportType;
    @Schema(example = "신고 유형 (기타)")
    String reportTypeEtc;
    @Schema(example = "신고 내용")
    String reportContent;

    public static ReportResponseDto of(Report report) {
        return ReportResponseDto.builder()
                .reportId(report.getId())
                .commentId(report.getReportedCommentId())
                .reportType(report.getReportType().getDescription())
                .reportTypeEtc(report.getReportEtc())
                .reportContent(report.getContent())
                .build();
    }
}
