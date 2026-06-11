package teamficial.teamficial_be.domain.recruitingPost.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import teamficial.teamficial_be.domain.recruitingPost.entity.Period;
import teamficial.teamficial_be.domain.recruitingPost.entity.ProgressWay;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingStatus;
import teamficial.teamficial_be.global.enums.Position;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RecruitingPostPagingDto {


    @Getter
    @Builder
    @AllArgsConstructor
    public static class RecruitingPostFlatDto {
        private Long postId;
        private Boolean profileIsDeleted;
        private Long writerUserId;
        private Long profileId;
        private String userName;
        private String profileImage;
        private ProgressWay progressWay;
        private String contactWay;
        private LocalDate startDate;
        private Period period;
        private LocalDate deadline;
        private RecruitingStatus status;
        private String content;
        private String title;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class RecruitingDetailFlatDto {
        private Long postId;
        private Position position;
        private Integer count;

    }
}
