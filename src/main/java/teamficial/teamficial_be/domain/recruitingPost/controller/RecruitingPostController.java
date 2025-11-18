package teamficial.teamficial_be.domain.recruitingPost.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import teamficial.teamficial_be.domain.recruitingPost.dto.RecruitingPostDto;
import teamficial.teamficial_be.domain.recruitingPost.entity.ProgressWay;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingStatus;
import teamficial.teamficial_be.domain.recruitingPost.service.RecruitingPostService;
import teamficial.teamficial_be.global.apiPayload.ApiResponse;
import teamficial.teamficial_be.global.enums.Position;
import teamficial.teamficial_be.global.security.AuthDetails;
import teamficial.teamficial_be.global.util.GlobalAuthUtil;

@RestController
@RequestMapping("/recruiting-posts")
@RequiredArgsConstructor
public class RecruitingPostController {

    private final RecruitingPostService recruitingPostService;

    @PostMapping
    @Operation(summary = "모집 글 작성 API", description = "프로젝트 모집 글 작성 API입니다.")
    public ApiResponse<RecruitingPostDto.RecruitingPostsResponseDTO> createPost(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestBody RecruitingPostDto.RecruitingPostRequestDTO dto) {

        Long userId = GlobalAuthUtil.extractUserId(authDetails);

        return ApiResponse.onSuccess(recruitingPostService.createPost(userId, dto)); // 생성된 게시글 ID 반환
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "모집 글 삭제 API", description = "프로젝트 모집 글 삭제 API입니다.")
    public ApiResponse<RecruitingPostDto.RecruitingPostDeleteResponseDTO> deletePost(
            @AuthenticationPrincipal AuthDetails authDetails,
            @PathVariable Long postId) {

        Long userId = GlobalAuthUtil.extractUserId(authDetails);

        return ApiResponse.onSuccess(recruitingPostService.deletePost(userId, postId));
    }

    @PatchMapping("/{postId}")
    @Operation(summary = "모집 글 수정 API", description = "프로젝트 모집 글 수정 API입니다.")
    public ApiResponse<RecruitingPostDto.RecruitingPostModifyResponseDTO> updatePost(
            @AuthenticationPrincipal AuthDetails authDetails,
            @PathVariable Long postId,
            @RequestBody RecruitingPostDto.RecruitingPostModifyRequestDTO dto) {

        Long userId = GlobalAuthUtil.extractUserId(authDetails);


        return ApiResponse.onSuccess(recruitingPostService.updatePost(userId, postId, dto));
    }

    @GetMapping("/{postId}")
    @Operation(summary = "모집 글 단일 조회 API", description = "프로젝트 모집 글 단일 조회 API입니다.")
    public ApiResponse<RecruitingPostDto.RecruitingPostsResponseDTO> getPost(
            @PathVariable Long postId) {

        return ApiResponse.onSuccess(recruitingPostService.getPost(postId));
    }

    @GetMapping
    @Operation(summary = "모집 글 전체 조회 API", description = "프로젝트 모집 글 전체 조회 API입니다.")
    public ApiResponse<Page<RecruitingPostDto.RecruitingPostsResponseDTO>> getRecruitingPosts(
            @RequestParam(required = false) RecruitingStatus status,
            @RequestParam(required = false) Position position,
            @RequestParam(required = false) ProgressWay progressWay,
            @PageableDefault(page = 0, size = 12, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return ApiResponse.onSuccess(
                recruitingPostService.getRecruitingPosts(status, position, progressWay, pageable)
        );
    }

}

