package teamficial.teamficial_be.domain.report.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import teamficial.teamficial_be.domain.report.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
    boolean existsByReportedCommentId(Long keywordCommentId);

    @Query("SELECT r FROM Report r " +
            "WHERE r.isApplied = false ")
    Slice<Report> findAllByIsApplied(Pageable pageable);
}
