package teamficial.teamficial_be.domain.keyword.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamficial.teamficial_be.domain.keyword.entity.HeadKeyword;
import teamficial.teamficial_be.domain.keyword.entity.Keyword;
import teamficial.teamficial_be.domain.keyword.entity.KeywordComment;
import teamficial.teamficial_be.domain.keyword.repository.KeywordCommentRepository;

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


}
