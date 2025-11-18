package teamficial.teamficial_be.domain.recruitingPost.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Period {
    WITHIN_1_MONTH("1개월 이내"),
    ONE_TO_THREE_MONTHS("1~3개월"),
    THREE_TO_SIX_MONTHS("3~6개월"),
    OVER_SIX_MONTHS("6개월 이상"),
    FLEXIBLE("미정/협의 예정");
    private final String description;
}
