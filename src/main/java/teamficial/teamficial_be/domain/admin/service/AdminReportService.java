package teamficial.teamficial_be.domain.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamficial.teamficial_be.domain.admin.dto.ReportResponseDto;
import teamficial.teamficial_be.domain.report.entity.Report;
import teamficial.teamficial_be.domain.report.service.ReportService;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.global.util.ScrollResponse;

@Service
@RequiredArgsConstructor
public class AdminReportService {

    private final ReportService reportService;

    @Transactional(readOnly = true)
    public ReportResponseDto getReport(User user, Long reportId) {
        Report report = reportService.getById(reportId);

        return ReportResponseDto.of(report);
    }


    @Transactional(readOnly = true)
    public ScrollResponse<ReportResponseDto> getReportList(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Slice<Report> reportList = reportService.getAllReport(pageable);

        Slice<ReportResponseDto> slice = reportList.map(ReportResponseDto::of);

        return ScrollResponse.of(slice);
    }

    @Transactional
    public void acceptReport(Long reportId) {
        Report report = reportService.getById(reportId);

        reportService.acceptReport(report);
    }
}
