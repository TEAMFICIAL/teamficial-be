package teamficial.teamficial_be.domain.recruitingPost.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamficial.teamficial_be.domain.recruitingDetail.entity.RecruitingDetail;
import teamficial.teamficial_be.domain.recruitingPost.entity.Period;
import teamficial.teamficial_be.domain.recruitingPost.entity.ProgressWay;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingStatus;
import teamficial.teamficial_be.global.enums.Position;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor
public class RecruitingPostDTO {

    @Getter
    @NoArgsConstructor
    public static class RecruitingPostRequestDTO {
        private Long profileId;
        private ProgressWay progressWay;
        private String contactWay;
        private LocalDateTime startDate;
        private Period period;
        private LocalDateTime deadline;
        private RecruitingStatus status;
        private String content;
        private String title;

        // 모집 직무 & 인원 리스트
        private List<RecruitingPositionDTO> recruitingPositions;

    }

    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class RecruitingPositionDTO {
        private Position position;
        private Integer count;

        public static RecruitingPositionDTO from(RecruitingDetail detail) {
            return RecruitingPositionDTO.builder()
                    .position(detail.getPosition())
                    .count(detail.getCount())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecruitingPostResponseDTO {
        private Long profileId;             // 작성자 프로필 ID
        private String profileName;         // 작성자 프로필 이름
        private ProgressWay progressWay;    // 진행 방식
        private String contactWay;          // 연락 방법
        private LocalDateTime startDate;    // 시작일
        private Period period;              // 진행 기간
        private LocalDateTime deadline;     // 모집 마감일
        private RecruitingStatus status;    // 모집 상태
        private String content;             // 프로젝트 설명
        private String title;

        // 모집 직무 & 인원 리스트
        private List<RecruitingPositionDTO> recruitingPositions;

        public static RecruitingPostResponseDTO from(RecruitingPost post, List<RecruitingDetail> recruitingDetails) {
            return RecruitingPostResponseDTO.builder()
                    .profileId(post.getProfile().getId())
                    .profileName(post.getProfile().getProfileName())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .progressWay(post.getProgressWay())
                    .period(post.getPeriod())
                    .status(post.getStatus())
                    .deadline(post.getDeadline())
                    .contactWay(post.getContactWay())
                    .recruitingPositions(
                            recruitingDetails != null
                                    ? recruitingDetails.stream()
                                    .map(RecruitingPositionDTO::from)
                                    .toList()
                                    : Collections.emptyList()
                    )
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecruitingPostDeleteResponseDTO {

        private Long deletedPostId;   // 삭제된 게시글 ID
        private String message;       // 응답 메시지

        public static RecruitingPostDeleteResponseDTO of(Long postId, String message) {
            return RecruitingPostDeleteResponseDTO.builder()
                    .deletedPostId(postId)
                    .message(message)
                    .build();
        }
    }
}
