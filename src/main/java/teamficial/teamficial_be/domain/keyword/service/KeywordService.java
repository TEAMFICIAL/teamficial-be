package teamficial.teamficial_be.domain.keyword.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamficial.teamficial_be.domain.keyword.dto.request.TeamficialLogRequestDto;
import teamficial.teamficial_be.domain.keyword.dto.response.KeywordContentPairDto;
import teamficial.teamficial_be.domain.keyword.entity.Keyword;
import teamficial.teamficial_be.domain.keyword.entity.KeywordComment;
import teamficial.teamficial_be.domain.keyword.repository.KeywordRepository;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.domain.user.service.UserService;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.handler.NotFoundHandler;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;
    private final UserService userService;
    private final KeywordCommentService keywordCommentService;

    @Transactional(readOnly = true)
    public Keyword getKeywordById(Long keywordId){

        return keywordRepository.findById(keywordId)
                .orElseThrow(()-> new NotFoundHandler(ErrorStatus.NOT_FOUND_KEYWORD));
    }

    @Transactional
    public void saveKeyword(Keyword keyword){
        keywordRepository.save(keyword);
    }

    @Transactional(readOnly = true)
    public Page<Keyword> getAllKeywordByUser(User user, Pageable pageable) {
        return keywordRepository.findAllByUser(user,pageable);
    }

    public Keyword upsertKeyword(User user, String bestKeyword) {

        return keywordRepository.findByUserAndKeywordName(user, bestKeyword)
                .map(existing -> {
                    existing.increaseCount();
                    return keywordRepository.save(existing);
                })
                .orElseGet(() -> {
                    Keyword newKeyword = Keyword.builder()
                            .user(user)
                            .keywordName(bestKeyword)
                            .count(1)
                            .is_head(false)
                            .build();

                    return keywordRepository.save(newKeyword);
                });
    }

    @Transactional
    public void saveBestKeyword(User owner, TeamficialLogRequestDto req, String content, String bestKeyword, List<KeywordContentPairDto> results) {
        Keyword keyword = upsertKeyword(owner, bestKeyword);

        keywordCommentService.save(
                KeywordComment.builder()
                        .keyword(keyword)
                        .content(content)
                        .build()
        );

        results.add(new KeywordContentPairDto(bestKeyword, content));
    }

    public List<Keyword> findRandomHeadKeywordsByUserId(Long userId) {
        return keywordRepository.findRandomHeadKeywordsByUserId(userId);
    }

    public Keyword getKeywordByUserAndKeywordName(User user,String keywordName) {
        return keywordRepository.findByUserAndKeywordName(user,keywordName)
                .orElseThrow(()-> new NotFoundHandler(ErrorStatus.NOT_FOUND_KEYWORD));
    }
}
