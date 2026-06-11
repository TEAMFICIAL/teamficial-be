package teamficial.teamficial_be.domain.profile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import teamficial.teamficial_be.domain.profile.dto.response.PreSignedUrlResponseDto;
import teamficial.teamficial_be.domain.profile.service.PreSignedUrlService;
import teamficial.teamficial_be.global.apiPayload.ApiResponse;

@RestController
@RequiredArgsConstructor
@Tag(name = "PreSignedUrl 발급", description = "PreSignedUrl 관련 API")
public class PreSignedUrlController {

    private final PreSignedUrlService preSignedUrlService;

    @PostMapping("/preSigned-url")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "이미지 업로드용 presigned url 발급", description = "이미지 업로드용 presigned url을 발급합니다.")
    public ApiResponse<PreSignedUrlResponseDto> createPreSignedUrl(@RequestParam String imageName) {
        PreSignedUrlResponseDto responseDto = preSignedUrlService.getPreSignedUrl("profile", imageName);
        return ApiResponse.onSuccess(responseDto);
    }
}
