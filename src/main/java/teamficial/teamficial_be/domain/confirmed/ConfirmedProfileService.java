package teamficial.teamficial_be.domain.confirmed;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamficial.teamficial_be.domain.confirmed.entity.ConfirmedProfile;
import teamficial.teamficial_be.domain.confirmed.entity.ConfirmedProfileLink;
import teamficial.teamficial_be.domain.confirmed.entity.ConfirmedProfileRepository;
import teamficial.teamficial_be.domain.profile.entity.Profile;

@Service
@RequiredArgsConstructor
public class ConfirmedProfileService {

    private final ConfirmedProfileRepository confirmedProfileRepository;

    public ConfirmedProfile createSnapshotFrom(Profile profile) {

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

        return confirmedProfileRepository.save(confirmed);
    }
}
