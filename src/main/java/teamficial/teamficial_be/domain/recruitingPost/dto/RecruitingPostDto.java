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
public class RecruitingPostDto {

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
        private List<RecruitingPositionDto> recruitingPositions;

    }

    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class RecruitingPositionDto {
        private Position position;
        private Integer count;

        public static RecruitingPositionDto from(RecruitingDetail detail) {
            return RecruitingPositionDto.builder()
                    .position(detail.getPosition())
                    .count(detail.getCount())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecruitingPositionQueryDTO {
        private Long postId;
        private Position position;
        private Integer count;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecruitingPostsResponseDTO {
        private Long postId;
        private Long writerUserId;
        private Long writerProfileId;
        private String userName;
        private String profileImageUrl;
        private boolean profileIsDeleted;
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
        private long dDay;
        private List<RecruitingPositionDto> recruitingPositions;

        public static RecruitingPostsResponseDTO from(RecruitingPost post, List<RecruitingDetail> recruitingDetails, long dDay) {
            return RecruitingPostsResponseDTO.builder()
                    .postId(post.getId())
                    .writerUserId(post.getUser().getId())
                    .writerProfileId(post.getProfile().getId())
                    .userName(post.getProfile().getUserName())
                    .profileImageUrl(post.getProfile().getProfileImage())
                    .profileIsDeleted(post.getProfile().isDeleted())
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
                                    .map(RecruitingPositionDto::from)
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

        public static RecruitingPostsResponseDTO from(
                RecruitingPostPagingDto.RecruitingPostFlatDto post,
                List<RecruitingPostDto.RecruitingPositionDto> positions,
                long dDay
        ) {
            return RecruitingPostsResponseDTO.builder()
                    .postId(post.getPostId())
                    .writerUserId(post.getWriterUserId())
                    .writerProfileId(post.getProfileId())
                    .userName(post.getUserName())
                    .profileImageUrl(post.getProfileImage())
                    .profileIsDeleted(post.getProfileIsDeleted())
                    .progressWay(post.getProgressWay())
                    .contactWay(post.getContactWay())
                    .startDate(post.getStartDate())
                    .period(post.getPeriod())
                    .deadline(post.getDeadline())
                    .status(post.getStatus())
                    .content(post.getContent())
                    .title(post.getTitle())
                    .createdAt(post.getCreatedAt())
                    .recruitingPositions(positions)
                    .dDay(dDay)
                    .build();
        }
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecruitingPostDetailResponseDTO {
        private Long postId;
        private ProfileResponseDto profile;
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
        private long dDay;
        private List<RecruitingPositionDto> recruitingPositions;

        public static RecruitingPostDetailResponseDTO from(RecruitingPost post, List<RecruitingDetail> recruitingDetails, long dDay) {
            return RecruitingPostDetailResponseDTO.builder()
                    .postId(post.getId())
                    .profile(ProfileResponseDto.of(post.getProfile(),post.getProfile().getHeadKeywords()))
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
                                    .map(RecruitingPositionDto::from)
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

        private Long deletedPostId;
        private String message;

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

        private List<RecruitingPositionDto> recruitingPositions;

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
        private List<RecruitingPositionDto> recruitingPositions;

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
                                    .map(RecruitingPositionDto::from)
                                    .toList()
                                    : Collections.emptyList()
                    )
                    .createdAt(post.getCreatedAt())
                    .build();

        }
    }



}
