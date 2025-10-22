package teamficial.teamficial_be.domain.recruitingPost.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecruitingStatus {
    OPEN("모집중"),
    CLOSED("모집완료");
    private final String description;
}
