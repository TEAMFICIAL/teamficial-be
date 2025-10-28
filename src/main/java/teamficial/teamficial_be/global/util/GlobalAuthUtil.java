package teamficial.teamficial_be.global.util;

import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;
import teamficial.teamficial_be.global.security.AuthDetails;

public class GlobalAuthUtil {
    private GlobalAuthUtil() {}

    public static Long extractUserId(AuthDetails authDetails) {
        if (authDetails == null) {
            throw new GeneralException(ErrorStatus._UNAUTHORIZED);
        }
        return authDetails.user().getId();
    }
}
