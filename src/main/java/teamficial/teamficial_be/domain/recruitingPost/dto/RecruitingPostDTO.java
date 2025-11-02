package teamficial.teamficial_be.domain.recruitingPost.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import teamficial.teamficial_be.domain.profile.dto.response.ProfileResponseDto;
import teamficial.teamficial_be.domain.recruitingDetail.entity.RecruitingDetail;
import teamficial.teamficial_be.domain.recruitingPost.entity.Period;
import teamficial.teamficial_be.domain.recruitingPost.entity.ProgressWay;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingStatus;
import teamficial.teamficial_be.global.enums.Position;

import java.time.LocalDate;
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
        private LocalDate startDate;
        private Period period;
        private LocalDate deadline;
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
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecruitingPostsResponseDTO {
        private Long postId;                // 모집 글 Id
        private Long profileId;
        private String userName;         // 작성자 이름
        private String profileImageUrl;     // profile image url

        private ProgressWay progressWay;    // 진행 방식
        private String contactWay;          // 연락 방법
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
        private LocalDate startDate;    // 시작일
        private Period period;              // 진행 기간
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
        private LocalDate deadline;     // 모집 마감일
        private RecruitingStatus status;    // 모집 상태
        private String content;             // 프로젝트 설명
        private String title;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss")
        private LocalDateTime createdAt;
        private long dDay;

        // 모집 직무 & 인원 리스트
        private List<RecruitingPositionDTO> recruitingPositions;

        public static RecruitingPostsResponseDTO from(RecruitingPost post, List<RecruitingDetail> recruitingDetails, long dDay) {
            return RecruitingPostsResponseDTO.builder()
                    .postId(post.getId())
                    .profileId(post.getProfile().getId())
                    .userName(post.getProfile().getUserName())
                    .profileImageUrl(post.getProfile().getProfileImage())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .progressWay(post.getProgressWay())
                    .period(post.getPeriod())
                    .status(post.getStatus())
                    .deadline(post.getDeadline())
                    .contactWay(post.getContactWay())
                    .startDate(post.getStartDate())
                    .recruitingPositions(
                            recruitingDetails != null
                                    ? recruitingDetails.stream()
                                    .map(RecruitingPositionDTO::from)
                                    .toList()
                                    : Collections.emptyList()
                    )
                    .createdAt(post.getCreatedAt())
                    .dDay(dDay)
                    .build();
        }

        public static RecruitingPostsResponseDTO from(RecruitingPost post, long dDay) {
            return from(post, post.getRecruitingDetails(), dDay);
        }

        @QueryProjection
        public RecruitingPostsResponseDTO(
                Long postId,
                Long profileId,
                String userName,
                String profileImageUrl,
                ProgressWay progressWay,
                String contactWay,
                LocalDate startDate,
                Period period,
                LocalDate deadline,
                RecruitingStatus status,
                String content,
                String title,
                LocalDateTime createdAt
        ) {
            this.postId = postId;
            this.profileId = profileId;
            this.userName = userName;
            this.profileImageUrl = profileImageUrl;
            this.progressWay = progressWay;
            this.contactWay = contactWay;
            this.startDate = startDate;
            this.period = period;
            this.deadline = deadline;
            this.status = status;
            this.content = content;
            this.title = title;
            this.createdAt = createdAt;
        }
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecruitingPostDetailResponseDTO {
        private Long postId;                // 모집 글 Id
        private ProfileResponseDto profile;

        private ProgressWay progressWay;    // 진행 방식
        private String contactWay;          // 연락 방법

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
        private LocalDate startDate;    // 시작일

        private Period period;              // 진행 기간

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
        private LocalDate deadline;     // 모집 마감일
        private RecruitingStatus status;    // 모집 상태
        private String content;             // 프로젝트 설명
        private String title;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss")
        private LocalDateTime createdAt;
        private long dDay;
        // 모집 직무 & 인원 리스트
        private List<RecruitingPositionDTO> recruitingPositions;

        public static RecruitingPostDetailResponseDTO from(RecruitingPost post, List<RecruitingDetail> recruitingDetails, long dDay) {
            return RecruitingPostDetailResponseDTO.builder()
                    .postId(post.getId())
                    .profile(ProfileResponseDto.of(post.getProfile()))
                    .title(post.getTitle())
                    .content(post.getContent())
                    .progressWay(post.getProgressWay())
                    .period(post.getPeriod())
                    .status(post.getStatus())
                    .deadline(post.getDeadline())
                    .contactWay(post.getContactWay())
                    .startDate(post.getStartDate())
                    .recruitingPositions(
                            recruitingDetails != null
                                    ? recruitingDetails.stream()
                                    .map(RecruitingPositionDTO::from)
                                    .toList()
                                    : Collections.emptyList()
                    )
                    .createdAt(post.getCreatedAt())
                    .dDay(dDay)
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

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecruitingPostModifyRequestDTO {
        private ProgressWay progressWay;
        private String contactWay;
        private LocalDate startDate;
        private Period period;
        private LocalDate deadline;
        private RecruitingStatus status;
        private String content;
        private String title;

        // 모집 직무 & 인원 리스트
        private List<RecruitingPositionDTO> recruitingPositions;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecruitingPostModifyResponseDTO {
        private ProgressWay progressWay;
        private String contactWay;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
        private LocalDate startDate;

        private Period period;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
        private LocalDate deadline;
        private RecruitingStatus status;
        private String content;
        private String title;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss")
        private LocalDateTime createdAt;

        // 모집 직무 & 인원 리스트
        private List<RecruitingPositionDTO> recruitingPositions;

        public static RecruitingPostModifyResponseDTO from(RecruitingPost post, List<RecruitingDetail> recruitingDetails) {
            return RecruitingPostModifyResponseDTO.builder()
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
                    .createdAt(post.getCreatedAt())
                    .build();

        }
    }



}
