package teamficial.teamficial_be.domain.keyword.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamficial.teamficial_be.domain.keyword.entity.Keyword;
import teamficial.teamficial_be.domain.keyword.repository.KeywordRepository;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.handler.NotFoundHandler;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;

    public Keyword getKeywordById(Long keywordId){

        return keywordRepository.findById(keywordId)
                .orElseThrow(()-> new NotFoundHandler(ErrorStatus.NOT_FOUND_KEYWORD));
    }

    public void saveKeyword(Keyword keyword){
        keywordRepository.save(keyword);
    }
}
