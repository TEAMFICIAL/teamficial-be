package teamficial.teamficial_be.domain.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamficial.teamficial_be.domain.application.dto.applicationDTO;
import teamficial.teamficial_be.domain.application.service.ApplicationService;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;
import teamficial.teamficial_be.global.security.AuthDetails;


@RestController
@RequiredArgsConstructor
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<applicationDTO.ApplicationResponseDTO> createApplication(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestBody applicationDTO.ApplicationRequestDTO req) {

        if (authDetails == null) {
            throw new GeneralException(ErrorStatus._UNAUTHORIZED);
        }

        Long userId = authDetails.user().getId();

        applicationDTO.ApplicationResponseDTO response = applicationService.createApplication(userId, req);

        return ResponseEntity.ok(response);
    }



}
