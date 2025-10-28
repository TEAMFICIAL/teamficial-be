package teamficial.teamficial_be.domain.application.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApplicationStatus {
    CONFIRMED("참여 확정"),
    OPEN("모집중"),
    CLOSED("모집완료");

    private final String description;
}
