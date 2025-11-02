package teamficial.teamficial_be.domain.recruitingPost.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecruitingPostsResponseDTO {
        private Long postId;                // 모집 글 Id
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
            System.out.println("=== [DEBUG 3] from(post, dDay) 내부 진입 ===");
            System.out.println("RecruitingPost class: " + post.getClass().getName());

            // 프로필 상태 확인
            System.out.println("isInitialized(profile): " + org.hibernate.Hibernate.isInitialized(post.getProfile()));
            System.out.println("profile class: " + post.getProfile().getClass().getName());

            // recruitingDetails 상태 확인
            System.out.println("isInitialized(recruitingDetails): " + org.hibernate.Hibernate.isInitialized(post.getRecruitingDetails()));
            System.out.println("recruitingDetails class: " + post.getRecruitingDetails().getClass().getName());

            // 실제 접근 시도 (여기서 쿼리가 발생할 수 있음)
            try {
                int size = post.getRecruitingDetails().size();
                System.out.println("recruitingDetails size: " + size + " ✅ 접근 성공");
            } catch (Exception e) {
                System.out.println("recruitingDetails 접근 중 예외 ❌: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            }

            System.out.println("=========================================");

            // 이 호출이 N+1 쿼리 발생 구간인지 확인
            return from(post, post.getRecruitingDetails(), dDay);
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
