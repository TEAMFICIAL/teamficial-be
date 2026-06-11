package teamficial.teamficial_be.domain.keyword.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamficial.teamficial_be.domain.keyword.entity.Keyword;
import teamficial.teamficial_be.domain.keyword.entity.KeywordComment;
import teamficial.teamficial_be.domain.keyword.repository.KeywordCommentRepository;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;

@Service
@RequiredArgsConstructor
public class KeywordCommentService {
    private final KeywordCommentRepository keywordCommentRepository;

    @Transactional(readOnly = true)
    public Slice<KeywordComment> getAllByKeyword(Keyword keyword, Pageable pageable) {
        return keywordCommentRepository.findAllByKeyword(keyword,pageable);
    }

    public void save(KeywordComment keywordComment) {
        keywordCommentRepository.save(keywordComment);
    }


    public KeywordComment getById(Long keywordCommentId) {
        return keywordCommentRepository.findById(keywordCommentId)
                .orElseThrow(()-> new GeneralException(ErrorStatus.NOT_FOUND_KEYWORD_COMMENT));
    }

    public void delete(KeywordComment comment) {
        keywordCommentRepository.delete(comment);
    }
}
