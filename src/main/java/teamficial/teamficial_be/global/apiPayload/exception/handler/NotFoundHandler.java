package teamficial.teamficial_be.global.apiPayload.exception.handler;

import teamficial.teamficial_be.global.apiPayload.code.BaseErrorCode;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;

public class NotFoundHandler extends GeneralException {
    public NotFoundHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
