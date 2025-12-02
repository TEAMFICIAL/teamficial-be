package teamficial.teamficial_be.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.domain.user.repository.UserRepository;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()->new GeneralException(ErrorStatus.NOT_FOUND_USER));
    }

    public User getUserByUuid(String requesterUuid) {
        return userRepository.findByUuidAndDeletedAtIsNull(requesterUuid)
                .orElseThrow(()->new GeneralException(ErrorStatus.NOT_FOUND_USER));
    }
}
