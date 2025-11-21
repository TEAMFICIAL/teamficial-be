package teamficial.teamficial_be.domain.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import teamficial.teamficial_be.domain.application.dto.ApplicationDTO;
import teamficial.teamficial_be.domain.application.service.ApplicationService;
import teamficial.teamficial_be.global.security.AuthDetails;
import teamficial.teamficial_be.global.util.GlobalAuthUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/applications")
@Tag(name = "지원하기 관련 API")
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    @Operation(summary = "모집글에 지원하기 API", description = "프로젝트 모집 글에 지원하기 API입니다.")
    public ResponseEntity<ApplicationDTO.ApplicationResponseDTO> createApplication(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestBody ApplicationDTO.ApplicationRequestDTO req) {

        Long userId = GlobalAuthUtil.extractUserId(authDetails);

        ApplicationDTO.ApplicationResponseDTO response = applicationService.createApplication(userId, req);

        return ResponseEntity.ok(response);
    }

}
