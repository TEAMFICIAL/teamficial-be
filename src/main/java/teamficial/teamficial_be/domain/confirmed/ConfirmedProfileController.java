package teamficial.teamficial_be.domain.confirmed;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import teamficial.teamficial_be.domain.confirmed.dto.response.ConfirmedProfileResponse;
import teamficial.teamficial_be.global.enums.Position;
import teamficial.teamficial_be.global.security.AuthDetails;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ConfirmedProfileController {

    private final ConfirmedProfileService confirmedProfileService;

    @GetMapping("/confirmed-profile/{postId}")
    public List<ConfirmedProfileResponse> getConfirmedProfiles(
            @AuthenticationPrincipal AuthDetails authDetails,
            @PathVariable Long postId,
            @RequestParam(required = false) Position position
    ) {
        return confirmedProfileService.getConfirmedProfileByPostId(authDetails.user(), postId, position);
    }


}
