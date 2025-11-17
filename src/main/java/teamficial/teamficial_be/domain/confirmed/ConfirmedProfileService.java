package teamficial.teamficial_be.domain.confirmed;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamficial.teamficial_be.domain.confirmed.entity.ConfirmedHeadKeyword;
import teamficial.teamficial_be.domain.confirmed.entity.ConfirmedProfile;
import teamficial.teamficial_be.domain.confirmed.entity.ConfirmedProfileLink;
import teamficial.teamficial_be.domain.confirmed.entity.ConfirmedProfileRepository;
import teamficial.teamficial_be.domain.profile.entity.Profile;

@Service
@RequiredArgsConstructor
public class ConfirmedProfileService {

    private final ConfirmedProfileRepository confirmedProfileRepository;

    @Transactional
    public void createSnapshotFrom(Profile profile) {

        ConfirmedProfile confirmed = ConfirmedProfile.builder()
                .userName(profile.getUser().getName())
                .profileName(profile.getProfileName())
                .profileImage(profile.getProfileImage())
                .workingTime(profile.getWorkingTime())
                .contactWay(profile.getContactWay())
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
}
