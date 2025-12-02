package teamficial.teamficial_be.domain.user.entity;

public enum LoginType {
    KAKAO("카카오"),
    GOOGLE("구글"),
    NAVER("네이버");

    private final String description;

    LoginType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
