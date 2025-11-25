package teamficial.teamficial_be.domain.confirmed.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import teamficial.teamficial_be.domain.confirmed.dto.response.ConfirmedProfileResponse;
import teamficial.teamficial_be.domain.confirmed.service.ConfirmedProfileService;
import teamficial.teamficial_be.global.enums.Position;
import teamficial.teamficial_be.global.security.AuthDetails;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "확정 프로필 관련 API(프로필 스냅샷)")
public class ConfirmedProfileController {

    private final ConfirmedProfileService confirmedProfileService;

    @GetMapping("/confirmed-profile/{postId}")
    @Operation(summary = "나의 팀 멤버 프로필 확인하기 API", description = "프로젝트 모집 완료 글의 멤버 프로필 조회 API입니다.")
    public List<ConfirmedProfileResponse> getConfirmedProfiles(
            @AuthenticationPrincipal AuthDetails authDetails,
            @PathVariable Long postId,
            @RequestParam(required = false) Position position
    ) {
        return confirmedProfileService.getConfirmedProfileByPostId(authDetails.user(), postId, position);
    }


}
