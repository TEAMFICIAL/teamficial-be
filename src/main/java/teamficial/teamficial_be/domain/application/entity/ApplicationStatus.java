package teamficial.teamficial_be.domain.application.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApplicationStatus {
    MATCHED("매칭 성공"),
    MATCHING("매칭중"),
    MATCH_FAILED("매칭 실패");

    private final String description;
}
