package teamficial.teamficial_be.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import teamficial.teamficial_be.domain.auth.dto.LoginResponseDTO;
import teamficial.teamficial_be.domain.auth.service.AuthService;
import teamficial.teamficial_be.global.apiPayload.ApiResponse;
import teamficial.teamficial_be.global.security.jwt.TokenProvider;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenProvider tokenProvider;

    @PostMapping("/auth/kakao")
    @Operation(summary = "카카오 로그인", description = "인가 코드를 통해 토큰을 발급받는 카카오 로그인 API입니다.")
    public ApiResponse<LoginResponseDTO.LoginTokenResponseDto> kakaoLogin(@RequestParam("code")String accessCode, @RequestParam("redirectUri") String redirectUri){
        return ApiResponse.onSuccess(authService.kakaoLogin(accessCode,redirectUri));
    }

    @PostMapping("/auth/google")
    @Operation(summary = "구글 로그인",description = "인가 코드를 통해 토큰을 발급받는 카카오 로그인 API입니다.")
    public ApiResponse<LoginResponseDTO.LoginTokenResponseDto> googleLogin(@RequestParam("code")String accessCode, @RequestParam("redirectUri") String redirectUri){
        return ApiResponse.onSuccess(authService.googleLogin(accessCode,redirectUri));
    }

}
