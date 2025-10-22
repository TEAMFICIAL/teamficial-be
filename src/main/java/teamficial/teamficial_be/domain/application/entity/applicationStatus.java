package teamficial.teamficial_be.domain.application.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum applicationStatus {
    WAITING("대기중"),
    OPEN("모집 미완료"),
    CLOSED("모집 완료");

    private final String description;
}
