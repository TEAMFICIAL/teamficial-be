package teamficial.teamficial_be.domain.profile.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorkingTime {
    MORNING("아침"),
    AFTERNOON("낮"),
    EVENING("밤"),
    DAWN("새벽");

    private final String description;
}
