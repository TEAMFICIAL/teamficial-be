package teamficial.teamficial_be.global.apiPayload.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import teamficial.teamficial_be.global.apiPayload.code.BaseErrorCode;
import teamficial.teamficial_be.global.apiPayload.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    REDIS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "REDIS_ERROR", "Redis 설정에 오류가 발생했습니다."),

    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "USER404", "해당 유저를 찾을 수 없습니다."),
    NOT_FOUND_TOKEN(HttpStatus.NOT_FOUND,"TOKEN404","토큰을 찾을 수 없습니다."),
    NOT_FOUND_PROFILE(HttpStatus.NOT_FOUND,"PROFILE404","해당 프로필을 찾을 수 없습니다."),
    NOT_FOUND_RECRUITING_POST(HttpStatus.NOT_FOUND,"RECRUITINGPOST404","해당 팀원 모집 글을 찾을 수 없습니다."),
    NOT_FOUND_APPLICAION(HttpStatus.NOT_FOUND,"APPLICATION404","해당 지원을 찾을 수 없습니다."),
    NOT_FOUND_KEYWORD(HttpStatus.NOT_FOUND,"KEYWORD404", "해당 키워드를 찾을 수 없습니다."),
    NOT_FOUND_HEAD_KEYWORD(HttpStatus.NOT_FOUND,"HEADKEYWORD404", "해당 대표 키워드를 찾을 수 없습니다."),
    NOT_FOUND_KEYWORD_COMMENT(HttpStatus.NOT_FOUND,"KEYWORDCOMMENT404", "해당 키워드 코멘트를 찾을 수 없습니다."),

    //로그인 관련 응답
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "LOGIN4001", "토큰이 유효하지 않습니다."),
    LOGIN_TYPE_INVALID(HttpStatus.BAD_REQUEST,"LOGIN4002","로그인 타입이 존재하지 않습니다."),

    //프로필 관련 응답
    FAILED_IMAGE_DELETE(HttpStatus.BAD_REQUEST,"PROFILE4001","이미지 삭제를 실패했습니다."),
    ALREADY_DELETED_PROFILE_IMAGE(HttpStatus.BAD_REQUEST, "PROFILE4002" , "프로필 사진이 이미 삭제된 상태입니다."),
    PROFILE_FORBIDDEN(HttpStatus.FORBIDDEN,"PROFILE4003","프로필 수정 권한이 없습니다."),
    CANNOT_DELETE_PROFILE_WHEN_APPLICATION_PENDING(HttpStatus.BAD_REQUEST,"PROFILE4004","해당 프로필로 지원중인 공고가 있어, 삭제가 불가능합니다."),
    CANNOT_DELETE_PROFILE_WHEN_RECRUITING_POST_OPEN(HttpStatus.BAD_REQUEST,"PROFILE4005","해당 프로필로 모집 중인 작성 글이 있어, 삭제가 불가능합니다."),
    CANNOT_COUNT_OVER_3(HttpStatus.BAD_REQUEST,"PROFILE4006","사용자의 프로필 개수는 3개를 넘을 수 없습니다."),
    CANNOT_DELETE_PROFILE(HttpStatus.BAD_REQUEST,"PROFILE4007","해당 프로필로 지원중인 공고나 모집 중인 작성 글이 있어, 삭제가 불가능합니다."),
    CANNOT_MODIFY_PROFILE_WHEN_APPLICATION_PENDING(HttpStatus.BAD_REQUEST,"PROFILE4008","해당 프로필로 지원중인 공고가 있어, 수정이 불가능합니다."),
    CANNOT_MODIFY_PROFILE_WHEN_RECRUITING_POST_OPEN(HttpStatus.BAD_REQUEST,"PROFILE4009","해당 프로필로 모집 중인 작성 글이 있어, 수정이 불가능합니다."),
    CANNOT_MODIFY_PROFILE(HttpStatus.BAD_REQUEST,"PROFILE4010","해당 프로필로 지원중인 공고나 모집 중인 작성 글이 있어, 수정이 불가능합니다."),

    //지원 관련 응답
    DUPLICATE_APPLICATION(HttpStatus.BAD_REQUEST,"APPLICATION6001","이미 지원한 모집 글 입니다."),
    CAN_NOT_APPLICATION(HttpStatus.BAD_REQUEST,"APPLICATION6002","대표 키워드를 등록하지않아 지원이 불가능합니다."),

    //키워드 관련 응답
    KEYWORD_FORBIDDEN(HttpStatus.FORBIDDEN,"KEYWORD4003","키워드 설정 권한이 없습니다."),
    CANNOT_HEAD_KEYWORD_OVER_3(HttpStatus.BAD_REQUEST,"HEADKEYWORD4004","프로필 당 대표키워드는 최대 3개를 넘을 수 없습니다."),
    HEAD_KEYWORD_DUPLICATE(HttpStatus.BAD_REQUEST,"HEADKEYWORD4005","이미 등록된 대표키워드입니다."),
    CAN_NOT_WRITE_TEAMFICIAL_LOG_OVER_1(HttpStatus.FORBIDDEN,"KEYWORD_COMMENT5001","해당 유저에게 쓴 팀피셜록이 이미 존재합니다."),

    //나의 팀 관련 응답
    TEAM_FORBIDDEN(HttpStatus.FORBIDDEN, "TEAM_FORBIDDEN403", "팀 멤버를 조회할 수 있는 권한이 없습니다."),

    //신고 관련 응답
    REPORT_DUPLICATE(HttpStatus.CONFLICT,"REPORT409","이미 신고된 코멘트입니다."),
    NOT_FOUND_REPORT(HttpStatus.NOT_FOUND, "REPORT404", "해당 신고를 찾을 수 없습니다."),
    REPORT_ALREADY_APPLIED(HttpStatus.CONFLICT,"REPORT4002","이미 반영된 신고입니다."),

    // 게시글 관련 응답
    POST_IMAGE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "POST4001", "이미지는 최대 2개까지 업로드 가능합니다.")

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
