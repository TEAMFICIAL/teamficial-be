package teamficial.teamficial_be.domain.keyword.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamficial.teamficial_be.domain.keyword.dto.HeadKeywordRequestDto;
import teamficial.teamficial_be.domain.keyword.dto.response.HeadKeywordResponseDto;
import teamficial.teamficial_be.domain.keyword.entity.HeadKeyword;
import teamficial.teamficial_be.domain.keyword.repository.HeadKeywordRepository;
import teamficial.teamficial_be.domain.profile.entity.Profile;
import teamficial.teamficial_be.domain.profile.service.ProfileService;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;

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


    public HeadKeywordResponseDto updateHeadKeyword(User user, Long profileId, HeadKeywordRequestDto requestDto) {

        Profile profile = profileService.getProfileById(profileId);

        if (!profile.getUser().equals(user)) {
            throw new GeneralException(ErrorStatus.PROFILE_FORBIDDEN);
        }

        if (profile.getHeadKeywords() !=null) {
            profile.getHeadKeywords().clear();
        }

        //대표키워드 설정
        if (requestDto.getKeywords() != null) {
            for (String keyword : requestDto.getKeywords()) {
                HeadKeyword headKeyword = HeadKeyword.builder()
                        .profile(profile)
                        .keywordName(keyword)
                        .build();
                profile.getHeadKeywords().add(headKeyword);
            }
        }

        profileService.saveProfile(profile);

        return HeadKeywordResponseDto.fromKeyword(profile, profile.getHeadKeywords());
    }
}
