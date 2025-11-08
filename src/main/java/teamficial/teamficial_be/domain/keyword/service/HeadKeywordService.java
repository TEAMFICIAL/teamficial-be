package teamficial.teamficial_be.domain.keyword.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamficial.teamficial_be.domain.keyword.entity.HeadKeyword;
import teamficial.teamficial_be.domain.keyword.repository.HeadKeywordRepository;
import teamficial.teamficial_be.domain.profile.entity.Profile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HeadKeywordService {

    private final HeadKeywordRepository headKeywordRepository;

    public List<HeadKeyword> getAllByProfile(Profile profile) {
        return headKeywordRepository.findAllByProfile(profile);
    }

    public void save(HeadKeyword headKeyword) {
        headKeywordRepository.save(headKeyword);
    }
}
