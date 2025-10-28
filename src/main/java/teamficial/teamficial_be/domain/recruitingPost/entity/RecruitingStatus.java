package teamficial.teamficial_be.domain.recruitingPost.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecruitingStatus {
    OPEN("모집중"),
    CONFIRMED("참여 확정"),
    CLOSED("모집 마감");
    private final String description;
}
