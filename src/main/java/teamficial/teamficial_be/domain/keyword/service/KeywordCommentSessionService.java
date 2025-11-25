package teamficial.teamficial_be.domain.keyword.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamficial.teamficial_be.domain.keyword.entity.KeywordCommentSession;
import teamficial.teamficial_be.domain.keyword.repository.KeywordCommentRepository;
import teamficial.teamficial_be.domain.keyword.repository.KeywordCommentSessionRepository;

@Service
@RequiredArgsConstructor
public class KeywordCommentSessionService {
    private final KeywordCommentSessionRepository keywordCommentSessionRepository;

    public void saveSession(KeywordCommentSession keywordCommentSession) {
        keywordCommentSessionRepository.save(keywordCommentSession);
    }

    public boolean existsByOwnerIdAndWriterId(Long ownerId, Long writerId) {
        return keywordCommentSessionRepository.existsByOwnerIdAndWriterId(ownerId, writerId);
    }
}
