package teamficial.teamficial_be.batch.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job postStatusUpdateJob; // job bean 주입

//    @Scheduled(cron = "*/30 * * * * *") //테스트용
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정 실행
    public void runPostStatusUpdateJob() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(postStatusUpdateJob, params);
            log.info("[Scheduler] 게시글 상태 업데이트 배치 실행 완료");
        } catch (Exception e) {
            log.error("[Scheduler] 게시글 상태 업데이트 배치 실행 실패", e);
        }
    }
}
