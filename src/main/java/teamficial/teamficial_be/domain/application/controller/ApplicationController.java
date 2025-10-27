package teamficial.teamficial_be.domain.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamficial.teamficial_be.domain.application.dto.ApplicationDTO;
import teamficial.teamficial_be.domain.application.service.ApplicationService;
import teamficial.teamficial_be.global.security.AuthDetails;
import teamficial.teamficial_be.global.util.GlobalAuthUtil;


@RestController
@RequiredArgsConstructor
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<ApplicationDTO.ApplicationResponseDTO> createApplication(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestBody ApplicationDTO.ApplicationRequestDTO req) {

        Long userId = GlobalAuthUtil.extractUserId(authDetails);

        ApplicationDTO.ApplicationResponseDTO response = applicationService.createApplication(userId, req);

        return ResponseEntity.ok(response);
    }



}
