package teamficial.teamficial_be.global.apiPayload.exception.handler;

import teamficial.teamficial_be.global.apiPayload.code.BaseErrorCode;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;

public class BadRequestHandler extends GeneralException {
    public BadRequestHandler(BaseErrorCode errorCode) {super(errorCode);}
}
