package teamficial.teamficial_be.domain.recruitingPost.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamficial.teamficial_be.domain.recruitingPost.dto.RecruitingPostDTO;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;
import teamficial.teamficial_be.domain.recruitingPost.service.RecruitingPostService;
import teamficial.teamficial_be.global.apiPayload.ApiResponse;

@RestController
@RequestMapping("/recruiting-posts")
@RequiredArgsConstructor
public class RecruitingPostController {

    private final RecruitingPostService recruitingPostService;

    @PostMapping
    @Operation(summary = "모집 글 작성 API", description = "프로젝트 모집 글 작성 API입니다.")
    public ApiResponse<RecruitingPostDTO.RecruitingPostResponseDTO> createPost(@RequestBody RecruitingPostDTO.RecruitingPostRequestDTO dto) {
        return ApiResponse.onSuccess(recruitingPostService.createPost(dto)); // 생성된 게시글 ID 반환
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "모집 글 삭제 API", description = "프로젝트 모집 글 삭제 API입니다.")
    public ApiResponse<RecruitingPostDTO.RecruitingPostDeleteResponseDTO> deletePost(@PathVariable Long postId) {
        return ApiResponse.onSuccess(recruitingPostService.deletePost(postId));
    }

    @PatchMapping("/{postId}")
    @Operation(summary = "모집 글 수정 API", description = "프로젝트 모집 글 수정 API입니다.")
    public ApiResponse<RecruitingPostDTO.RecruitingPostResponseDTO> updatePost(
            @PathVariable Long postId,
            @RequestBody RecruitingPostDTO.RecruitingPostRequestDTO dto) {
        return ApiResponse.onSuccess(recruitingPostService.updatePost(postId, dto));
    }

    @GetMapping("/{postId}")
    @Operation(summary = "모집 글 단일 조회 API", description = "프로젝트 모집 글 단일 조회 API입니다.")
    public ApiResponse<RecruitingPostDTO.RecruitingPostResponseDTO> getPost(@PathVariable Long postId) {
        return ApiResponse.onSuccess(recruitingPostService.getPost(postId));
    }
}
