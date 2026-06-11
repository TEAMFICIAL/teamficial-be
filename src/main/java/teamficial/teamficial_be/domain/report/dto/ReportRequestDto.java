package teamficial.teamficial_be.domain.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import teamficial.teamficial_be.domain.report.entity.ReportType;

@Getter
public class ReportRequestDto {
    @NotNull(message = "신고 유형은 필수입니다.")
    private ReportType reportType;

    @Schema(example = "신고 유형이 기타일 경우 입력해주세요.")
    @Size(max = 100)
    private String reportEtc; //신고 유형이 기타일 경우

    @NotBlank(message = "신고 내용은 필수입니다.")
    @Size(max = 500)
    private String content;

}
