package teamficial.teamficial_be.domain.confirmed;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamficial.teamficial_be.domain.confirmed.dto.response.ConfirmedProfileResponse;
import teamficial.teamficial_be.domain.confirmed.entity.ConfirmedHeadKeyword;
import teamficial.teamficial_be.domain.confirmed.entity.ConfirmedProfile;
import teamficial.teamficial_be.domain.confirmed.entity.ConfirmedProfileLink;
import teamficial.teamficial_be.domain.confirmed.repository.ConfirmedProfileRepository;
import teamficial.teamficial_be.domain.profile.entity.Profile;
import teamficial.teamficial_be.domain.profile.service.ProfileService;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;
import teamficial.teamficial_be.domain.recruitingPost.service.RecruitingPostService;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.global.enums.Position;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfirmedProfileService {

    private final ConfirmedProfileRepository confirmedProfileRepository;
    private final ProfileService profileService;
    private final RecruitingPostService recruitingPostService;

    @Transactional
    public void createSnapshotFrom(Profile profile, Position position, RecruitingPost recruitingPost) {

        ConfirmedProfile confirmed = ConfirmedProfile.builder()
                .recruitingPost(recruitingPost)
                .userName(profile.getUser().getName())
                .profileName(profile.getProfileName())
                .profileImage(profile.getProfileImage())
                .workingTime(profile.getWorkingTime())
                .contactWay(profile.getContactWay())
                .position(position)
                .build();

        profile.getProfileLinks().forEach(link ->
                confirmed.addProfileLink(
                        ConfirmedProfileLink.builder()
                                .confirmedProfile(confirmed)
                                .link(link.getLink())
                                .build()
                )
        );

        profile.getHeadKeywords().forEach(hk ->
                confirmed.addHeadKeyword(
                        ConfirmedHeadKeyword.builder()
                                .confirmedProfile(confirmed)
                                .keywordName(hk.getKeywordName())
                                .build()
                )
        );
        confirmedProfileRepository.save(confirmed);
    }

    @Transactional(readOnly = true)
    public List<ConfirmedProfileResponse> getConfirmedProfileByPostId(User user, Long postId, Position position) {
        RecruitingPost recruitingPost = recruitingPostService.getRecruitingPostById(postId);
        profileService.validateUserProfile(user,recruitingPost.getProfile());

        List<ConfirmedProfile> confirmedProfiles =
                confirmedProfileRepository.findByPostIdAndPosition(postId, position);

        return confirmedProfiles.stream()
                .map(ConfirmedProfileResponse::from)
                .toList();
    }
}
