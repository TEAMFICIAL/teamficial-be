package teamficial.teamficial_be.domain.keyword.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamficial.teamficial_be.domain.keyword.dto.request.HeadKeywordRequestDto;
import teamficial.teamficial_be.domain.keyword.dto.response.HeadKeywordResponseDto;
import teamficial.teamficial_be.domain.keyword.entity.HeadKeyword;
import teamficial.teamficial_be.domain.keyword.entity.Keyword;
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


    private final ProfileService profileService;
    private final KeywordService keywordService;
    private final HeadKeywordService headKeywordService;

    @Transactional(readOnly = true)
    public HeadKeywordResponseDto getHeadKeyword(User user, Long profileId) {
        Profile profile = profileService.getProfileById(profileId);

        List<HeadKeyword> headKeywords= headKeywordService.getAllByProfile(profile);

        return HeadKeywordResponseDto.fromKeyword(profile, headKeywords);
    }


    @Transactional
    public HeadKeywordResponseDto updateHeadKeyword(User user, Long profileId, HeadKeywordRequestDto requestDto) {

        Profile profile = profileService.getProfileById(profileId);

        if (!profile.getUser().equals(user)) {
            throw new GeneralException(ErrorStatus.PROFILE_FORBIDDEN);
        }

        if (profile.getHeadKeywords() !=null) {
            profile.getHeadKeywords().clear();
        }

        //대표키워드 설정
        if (requestDto.getKeywordIds() != null) {
            for (Long keywordId : requestDto.getKeywordIds()) {

                Keyword keyword = keywordService.getKeywordById(keywordId);
                keyword.updateHead();

                HeadKeyword headKeyword = HeadKeyword.builder()
                        .profile(profile)
                        .keywordName(keyword.getKeywordName())
                        .build();

                headKeywordService.save(headKeyword);
                keywordService.saveKeyword(keyword);

                profile.getHeadKeywords().add(headKeyword);
            }
        }

        profileService.saveProfile(profile);

        return HeadKeywordResponseDto.fromKeyword(profile, profile.getHeadKeywords());
    }
}
