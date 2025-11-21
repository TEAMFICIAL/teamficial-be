package teamficial.teamficial_be.domain.keyword.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamficial.teamficial_be.domain.keyword.entity.HeadKeyword;
import teamficial.teamficial_be.domain.keyword.repository.HeadKeywordRepository;
import teamficial.teamficial_be.domain.profile.entity.Profile;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.handler.NotFoundHandler;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HeadKeywordService {

    private final HeadKeywordRepository headKeywordRepository;

    @Transactional(readOnly = true)
    public List<HeadKeyword> getAllByProfile(Profile profile) {
        return headKeywordRepository.findAllByProfile(profile);
    }

    @Transactional
    public void save(HeadKeyword headKeyword) {
        headKeywordRepository.save(headKeyword);
    }

    public HeadKeyword getHeadKeywordById(Long oldHeadKeywordId) {
        return headKeywordRepository.findById(oldHeadKeywordId)
                .orElseThrow(()-> new NotFoundHandler(ErrorStatus.NOT_FOUND_HEAD_KEYWORD));
    }

    public void delete(HeadKeyword oldHeadKeyword) {
        headKeywordRepository.delete(oldHeadKeyword);
    }

    public int countHeadKeyword(Profile profile) {
        return headKeywordRepository.countByProfile(profile);
    }
}
