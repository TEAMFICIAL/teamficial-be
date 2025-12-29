package teamficial.teamficial_be.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamficial.teamficial_be.domain.keyword.entity.KeywordComment;
import teamficial.teamficial_be.domain.keyword.service.KeywordCommentService;
import teamficial.teamficial_be.domain.report.dto.ReportRequestDto;
import teamficial.teamficial_be.domain.report.entity.Report;
import teamficial.teamficial_be.domain.report.entity.ReportType;
import teamficial.teamficial_be.domain.report.repository.ReportRepository;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final KeywordCommentService keywordCommentService;

    @Transactional
    public void reportTeamficialLog(User user, Long keywordCommentId, ReportRequestDto reportRequestDto) {
        KeywordComment comment = keywordCommentService.getById(keywordCommentId);

        if (!comment.getKeyword().getUser().getId().equals(user.getId())) {
            throw new GeneralException(ErrorStatus._FORBIDDEN);
        }

        Report report = Report.builder()
                .reportType(reportRequestDto.getReportType())
                .reportEtc(reportRequestDto.getReportType()== ReportType.OTHER? reportRequestDto.getReportEtc() : null)
                .content(reportRequestDto.getContent())
                .isApplied(false)
                .user(user)
                .build();

        reportRepository.save(report);

        //이메일 전송 로직
    }
}
