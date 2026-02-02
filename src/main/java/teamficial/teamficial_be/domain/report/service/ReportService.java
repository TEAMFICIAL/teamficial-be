package teamficial.teamficial_be.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamficial.teamficial_be.domain.keyword.entity.Keyword;
import teamficial.teamficial_be.domain.keyword.entity.KeywordComment;
import teamficial.teamficial_be.domain.keyword.service.KeywordCommentService;
import teamficial.teamficial_be.domain.keyword.service.KeywordService;
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
    private final MailService mailService;
    private final KeywordService keywordService;

    @Transactional
    public void reportTeamficialLog(User user, Long keywordCommentId, ReportRequestDto reportRequestDto) {
        KeywordComment comment = keywordCommentService.getById(keywordCommentId);

        //중복 신고 방지
        if (alreadyExistReport(keywordCommentId, user.getId())){
            throw new GeneralException(ErrorStatus.REPORT_DUPLICATE);
        }

        //권한 확인
        if (!comment.getKeyword().getUser().getId().equals(user.getId())) {
            throw new GeneralException(ErrorStatus._FORBIDDEN);
        }

        Report report = Report.builder()
                .reportType(reportRequestDto.getReportType())
                .reportEtc(reportRequestDto.getReportType()== ReportType.OTHER? reportRequestDto.getReportEtc() : null)
                .content(reportRequestDto.getContent())
                .reportedCommentId(keywordCommentId)
                .isApplied(false)
                .user(user)
                .build();

        try {
            reportRepository.save(report);
        } catch (DataIntegrityViolationException e) {
            throw new GeneralException(ErrorStatus.REPORT_DUPLICATE);
        }

        //이메일 전송 로직
        mailService.sendReportEmail(user.getEmail());
    }

    @Transactional
    public void acceptReport(Report report) {

        if (report.isApplied()) {
            throw new GeneralException(ErrorStatus.REPORT_ALREADY_APPLIED);
        }

        //코멘트 삭제
        KeywordComment comment = keywordCommentService.getById(report.getReportedCommentId());

        keywordCommentService.delete(comment);

        //신고된 코멘트로 인해 추출된 키워드의 코멘트가 신고된 코멘트밖에 없는 경우
        Keyword keyword = comment.getKeyword();

        keyword.decreaseCount();
        if (keyword.getCount() ==0){
            keywordService.delete(keyword);
        }

        report.reportAccept();

        reportRepository.save(report);
    }

    @Transactional(readOnly = true)
    public Report getById(Long reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(()-> new GeneralException(ErrorStatus.NOT_FOUND_REPORT));
    }

    @Transactional(readOnly = true)
    public Slice<Report> getAllReport(Pageable pageable) {
        return reportRepository.findAllByIsApplied(pageable);
    }

    private boolean alreadyExistReport(Long keywordCommentId, Long userId) {
        return reportRepository.existsByReportedCommentIdAndUserId(keywordCommentId, userId);
    }
}
