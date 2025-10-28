package teamficial.teamficial_be.domain.recruitingPost.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import teamficial.teamficial_be.domain.recruitingPost.dto.RecruitingPostDTO;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;
import teamficial.teamficial_be.domain.recruitingPost.service.RecruitingPostService;
import teamficial.teamficial_be.global.apiPayload.ApiResponse;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;
import teamficial.teamficial_be.global.security.AuthDetails;
import teamficial.teamficial_be.global.util.GlobalAuthUtil;

@RestController
@RequestMapping("/recruiting-posts")
@RequiredArgsConstructor
public class RecruitingPostController {

    private final RecruitingPostService recruitingPostService;

    @PostMapping
    @Operation(summary = "모집 글 작성 API", description = "프로젝트 모집 글 작성 API입니다.")
    public ApiResponse<RecruitingPostDTO.RecruitingPostResponseDTO> createPost(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestBody RecruitingPostDTO.RecruitingPostRequestDTO dto) {

        Long userId = GlobalAuthUtil.extractUserId(authDetails);

        return ApiResponse.onSuccess(recruitingPostService.createPost(userId, dto)); // 생성된 게시글 ID 반환
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "모집 글 삭제 API", description = "프로젝트 모집 글 삭제 API입니다.")
    public ApiResponse<RecruitingPostDTO.RecruitingPostDeleteResponseDTO> deletePost(
            @AuthenticationPrincipal AuthDetails authDetails,
            @PathVariable Long postId) {

        Long userId = GlobalAuthUtil.extractUserId(authDetails);

        return ApiResponse.onSuccess(recruitingPostService.deletePost(postId, userId));
    }

    @PatchMapping("/{postId}")
    @Operation(summary = "모집 글 수정 API", description = "프로젝트 모집 글 수정 API입니다.")
    public ApiResponse<RecruitingPostDTO.RecruitingPostModifyResponseDTO> updatePost(
            @AuthenticationPrincipal AuthDetails authDetails,
            @PathVariable Long postId,
            @RequestBody RecruitingPostDTO.RecruitingPostModifyRequestDTO dto) {

        Long userId = GlobalAuthUtil.extractUserId(authDetails);


        return ApiResponse.onSuccess(recruitingPostService.updatePost(userId, postId, dto));
    }

    @GetMapping("/{postId}")
    @Operation(summary = "모집 글 단일 조회 API", description = "프로젝트 모집 글 단일 조회 API입니다.")
    public ApiResponse<RecruitingPostDTO.RecruitingPostResponseDTO> getPost(
            @AuthenticationPrincipal AuthDetails authDetails,
            @PathVariable Long postId) {

        Long userId = GlobalAuthUtil.extractUserId(authDetails);

        return ApiResponse.onSuccess(recruitingPostService.getPost(userId, postId));
    }


}

