package teamficial.teamficial_be.domain.keyword.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamficial.teamficial_be.domain.keyword.dto.request.HeadKeywordRequestDto;
import teamficial.teamficial_be.domain.keyword.dto.response.HeadKeywordResponseDto;
import teamficial.teamficial_be.domain.keyword.dto.response.KeywordCommentResponseDto;
import teamficial.teamficial_be.domain.keyword.dto.response.KeywordResponseDto;
import teamficial.teamficial_be.domain.keyword.entity.HeadKeyword;
import teamficial.teamficial_be.domain.keyword.entity.Keyword;
import teamficial.teamficial_be.domain.keyword.entity.KeywordComment;
import teamficial.teamficial_be.domain.profile.entity.Profile;
import teamficial.teamficial_be.domain.profile.service.ProfileService;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.domain.user.service.UserService;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;
import teamficial.teamficial_be.global.util.PagedResponse;
import teamficial.teamficial_be.global.util.ScrollResponse;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamficialLogService {


    private final ProfileService profileService;
    private final KeywordService keywordService;
    private final HeadKeywordService headKeywordService;
    private final UserService userService;
    private final KeywordCommentService keywordCommentService;

    @Transactional(readOnly = true)
    public HeadKeywordResponseDto getHeadKeyword(User user, Long profileId) {
        Profile profile = profileService.getProfileById(profileId);

        List<HeadKeyword> headKeywords= headKeywordService.getAllByProfile(profile);

        return HeadKeywordResponseDto.fromKeyword(profile, headKeywords);
    }


    @Transactional
    public HeadKeywordResponseDto updateHeadKeyword(User user, Long profileId, HeadKeywordRequestDto requestDto) {

        Profile profile = profileService.getProfileById(profileId);

        if (!profile.getUser().getId().equals(user.getId())) {
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
                keywordService.saveKeyword(keyword);

                HeadKeyword headKeyword = HeadKeyword.builder()
                        .profile(profile)
                        .keywordName(keyword.getKeywordName())
                        .build();

                profile.getHeadKeywords().add(headKeyword);
                log.info("headKeyword: {}", headKeyword.getKeywordName());

            }
        }

        profileService.saveProfile(profile);

        return HeadKeywordResponseDto.fromKeyword(profile, profile.getHeadKeywords());
    }

    @Transactional(readOnly = true)
    public PagedResponse<KeywordResponseDto> getKeywordList(Long userId, int page, int size) {
        User user = userService.getUserById(userId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Keyword> keywordPage = keywordService.getAllKeywordByUser(user,pageable);

        Page<KeywordResponseDto> dtoPage = keywordPage.map(KeywordResponseDto::from);

        return PagedResponse.of(dtoPage);
    }

    @Transactional(readOnly = true)
    public ScrollResponse<KeywordCommentResponseDto> getKeywordCommentList(Long keywordId, int page, int size) {
        Keyword keyword = keywordService.getKeywordById(keywordId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Slice<KeywordComment> keywordComments = keywordCommentService.getAllByKeyword(keyword,pageable);

        Slice<KeywordCommentResponseDto> dtoList = keywordComments.map(KeywordCommentResponseDto::from);

        return ScrollResponse.of(dtoList);
    }
}
