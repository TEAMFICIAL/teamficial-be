package teamficial.teamficial_be.domain.recruitingPost.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProgressWay {
    ONLINE("온라인"),
    OFFLINE("오프라인"),
    HYBRID("온/오프라인");

    private final String description;
}
