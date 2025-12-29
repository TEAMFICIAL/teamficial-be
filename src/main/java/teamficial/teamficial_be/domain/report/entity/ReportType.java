package teamficial.teamficial_be.domain.report.entity;

public enum ReportType {
    HATE_SPEECH("비방적인 내용입니다."),
    UNSUITABLE_KEYWORD("적합하지 않은 내용의 키워드입니다."),
    OTHER("기타 (직접 입력)");

    private final String description;

    ReportType(String description) {this.description = description;}

    public String getDescription() {return description;}
}
