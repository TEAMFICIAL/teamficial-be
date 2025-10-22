package teamficial.teamficial_be.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Position {
    FRONTEND("프론트엔드"),
    BACKEND("백엔드"),
    UI_UX("UI/UX 디자인"),
    AI("AI"),
    ANDROID("안드로이드"),
    IOS("ios"),
    PLANNER("기획자"),
    MARKETER("마케터"),
    PM("PM"),
    CLOUD_INFRA("클라우드/인프라"),
    DEV_OPS("데브옵스"),
    ETC("기타");

    private final String description;
}