package teamficial.teamficial_be.domain.keyword.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamficial.teamficial_be.domain.keyword.entity.Keyword;
import teamficial.teamficial_be.domain.keyword.repository.KeywordRepository;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.handler.NotFoundHandler;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;

    @Transactional(readOnly = true)
    public Keyword getKeywordById(Long keywordId){

        return keywordRepository.findById(keywordId)
                .orElseThrow(()-> new NotFoundHandler(ErrorStatus.NOT_FOUND_KEYWORD));
    }

    @Transactional
    public void saveKeyword(Keyword keyword){
        keywordRepository.save(keyword);
    }

    @Transactional
    public Page<Keyword> getAllKeywordByUser(User user, Pageable pageable) {
        return keywordRepository.findAllByUser(user,pageable);
    }
}
