package teamficial.teamficial_be.batch.step;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import teamficial.teamficial_be.batch.tasklet.PostStatusUpdateTasklet;

@Configuration
@RequiredArgsConstructor
public class PostStatusUpdateStepConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final PostStatusUpdateTasklet postStatusUpdateTasklet;

    @Bean
    public Step postStatusUpdateStep() {
        return new StepBuilder("postStatusUpdateStep", jobRepository)
                .tasklet(postStatusUpdateTasklet, transactionManager)
                .build();
    }
}