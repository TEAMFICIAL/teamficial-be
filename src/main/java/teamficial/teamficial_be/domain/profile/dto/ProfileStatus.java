package teamficial.teamficial_be.domain.profile.dto;

import lombok.Builder;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;

@Builder
public record ProfileStatus(boolean hasAnyApplication, boolean hasMatchingApplication, boolean hasAnyPost,
                            boolean hasOpenPost) {
    public boolean canModifyOrDelete() {
        if (!hasAnyApplication && !hasAnyPost) {
            return true;
        }

        boolean applicationAllowsModification = !hasAnyApplication || !hasMatchingApplication;

        boolean postAllowsModification = !hasAnyPost || !hasOpenPost;

        return applicationAllowsModification && postAllowsModification;
    }

    public boolean canHardDelete() {
        return !hasAnyApplication && !hasAnyPost;
    }

    public void validateForModification() {
        if (hasMatchingApplication) {
            throw new GeneralException(ErrorStatus.CANNOT_MODIFY_PROFILE_WHEN_APPLICATION_PENDING);
        }
        if (hasOpenPost) {
            throw new GeneralException(ErrorStatus.CANNOT_MODIFY_PROFILE_WHEN_RECRUITING_POST_OPEN);
        }
    }

    public void validateForDeletion() {
        if (hasMatchingApplication) {
            throw new GeneralException(ErrorStatus.CANNOT_DELETE_PROFILE_WHEN_APPLICATION_PENDING);
        }
        if (hasOpenPost) {
            throw new GeneralException(ErrorStatus.CANNOT_DELETE_PROFILE_WHEN_RECRUITING_POST_OPEN);
        }
    }
}
