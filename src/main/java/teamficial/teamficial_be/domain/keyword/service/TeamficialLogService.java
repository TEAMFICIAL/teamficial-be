package teamficial.teamficial_be.domain.keyword.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamficial.teamficial_be.domain.keyword.dto.response.HeadKeywordResponseDto;
import teamficial.teamficial_be.domain.keyword.entity.HeadKeyword;
import teamficial.teamficial_be.domain.keyword.repository.HeadKeywordRepository;
import teamficial.teamficial_be.domain.profile.entity.Profile;
import teamficial.teamficial_be.domain.profile.service.ProfileService;
import teamficial.teamficial_be.domain.user.entity.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamficialLogService {

    private final HeadKeywordRepository headKeywordRepository;
    private final ProfileService profileService;

    public HeadKeywordResponseDto getHeadKeyword(User user, Long profileId) {
        Profile profile = profileService.getProfileById(profileId);

        List<HeadKeyword> headKeywords= headKeywordRepository.findAllByProfile(profile);

        return HeadKeywordResponseDto.fromKeyword(profile, headKeywords);
    }


}
