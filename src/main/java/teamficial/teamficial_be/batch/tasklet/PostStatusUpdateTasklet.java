package teamficial.teamficial_be.batch.tasklet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import teamficial.teamficial_be.domain.recruitingPost.repository.RecruitingPostRepository;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostStatusUpdateTasklet implements Tasklet {

    private final RecruitingPostRepository recruitingPostRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        LocalDate today = LocalDate.now();
        int updatedCount = recruitingPostRepository.updateStatusToClosed(today);
        log.info("[Batch] 마감일이 지난 게시글 {}건 CLOSED 처리 완료", updatedCount);
        return RepeatStatus.FINISHED;
    }
}