package teamficial.teamficial_be.domain.recruitingPost.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import teamficial.teamficial_be.domain.profile.dto.response.PreSignedUrlResponseDto;
import teamficial.teamficial_be.domain.profile.service.PreSignedUrlService;
import teamficial.teamficial_be.global.apiPayload.ApiResponse;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post-images")
public class ImageUploadController {

    private final PreSignedUrlService preSignedUrlService;

    @PostMapping("/presigned-url")
    public ApiResponse<List<PreSignedUrlResponseDto>> getPresignedUrls(
            @RequestParam List<String> fileNames
    ) {
        if (fileNames.size() > 2) {
            throw new GeneralException(ErrorStatus.POST_IMAGE_LIMIT_EXCEEDED);
        }

        List<PreSignedUrlResponseDto> urls = fileNames.stream()
                .map(name -> preSignedUrlService.getPreSignedUrl("post/temp", name))
                .toList();

        return ApiResponse.onSuccess(urls);
    }
}