package teamficial.teamficial_be.domain.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teamficial.teamficial_be.domain.report.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
