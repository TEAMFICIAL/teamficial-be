package teamficial.teamficial_be.domain.application.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApplicationStatus {
    MATCHED("매칭성공"),
    TEMP_SAVED("임시저장"),
    MATCHING("매칭중"),
    MATCH_FAILED("매칭실패");

    private final String description;
}
