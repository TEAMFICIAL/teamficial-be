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

    //로그인 관련 응답
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "LOGIN4001", "토큰이 유효하지 않습니다."),
    LOGIN_TYPE_INVALID(HttpStatus.BAD_REQUEST,"LOGIN4002","로그인 타입이 존재하지 않습니다."),

    //프로필 관련 응답
    FAILED_IMAGE_DELETE(HttpStatus.BAD_REQUEST,"PROFILE4001","이미지 삭제를 실패했습니다."),
    ALREADY_DELETED_PROFILE_IMAGE(HttpStatus.BAD_REQUEST, "PROFILE4002" , "프로필 사진이 이미 삭제된 상태입니다."),
    PROFILE_FORBIDDEN(HttpStatus.FORBIDDEN,"PROFILE4003","프로필 수정 권한이 없습니다."),

    //지원 관련 응답
    DUPLICATE_APPLICATION(HttpStatus.BAD_REQUEST,"APPLICATION6001","이미 지원한 모집 글 입니다."),

    //키워드 관련 응답
    KEYWORD_FORBIDDEN(HttpStatus.FORBIDDEN,"KEYWORD4003","키워드 설정 권한이 없습니다."),
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
