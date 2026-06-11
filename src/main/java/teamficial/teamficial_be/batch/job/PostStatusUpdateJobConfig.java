package teamficial.teamficial_be.batch.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import teamficial.teamficial_be.batch.step.PostStatusUpdateStepConfig;

@Configuration
@RequiredArgsConstructor
public class PostStatusUpdateJobConfig {

    private final JobRepository jobRepository;
    private final PostStatusUpdateStepConfig stepConfig;

    @Bean
    public Job postStatusUpdateJob() {
        return new JobBuilder("postStatusUpdateJob", jobRepository)
                .start(stepConfig.postStatusUpdateStep())
                .build();
    }
}