package teamficial.teamficial_be.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import teamficial.teamficial_be.domain.auth.dto.LoginResponseDTO;
import teamficial.teamficial_be.domain.auth.service.AuthService;
import teamficial.teamficial_be.global.apiPayload.ApiResponse;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;
import teamficial.teamficial_be.global.security.AuthDetails;
import teamficial.teamficial_be.global.security.jwt.CookieUtil;
import teamficial.teamficial_be.global.security.jwt.TokenProvider;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenProvider tokenProvider;
    private final CookieUtil cookieUtil;

    @PostMapping("/auth/kakao")
    @Operation(summary = "카카오 로그인", description = "인가 코드를 통해 토큰을 발급받는 카카오 로그인 API입니다.")
    public ApiResponse<LoginResponseDTO.LoginTokenResponseDto> kakaoLogin(@RequestParam String accessCode, @RequestParam String redirectUri) {
        return ApiResponse.onSuccess(authService.kakaoLogin(accessCode, redirectUri));
    }


    @PostMapping("/auth/google")
    @Operation(summary = "구글 로그인", description = "인가 코드를 통해 토큰을 발급받는 구글 로그인 API입니다.")
    public ApiResponse<LoginResponseDTO.LoginTokenResponseDto> googleLogin(@RequestParam String accessCode, @RequestParam String redirectUri) {
        return ApiResponse.onSuccess(authService.googleLogin(accessCode, redirectUri));
    }

    @PostMapping("/auth/naver")
    @Operation(summary = "네이버 로그인", description = "인가 코드를 통해 토큰을 발급받는 네이버 로그인 API입니다.")
    public ApiResponse<LoginResponseDTO.LoginTokenResponseDto> naverLogin (
            @RequestParam("code") String accessCode,
            @RequestParam("state") String state,
            @RequestParam("redirectUri") String redirectUri){
        return ApiResponse.onSuccess(authService.naverLogin(accessCode, state, redirectUri));
    }

    @GetMapping("/auth/refresh-token")
    @Operation(summary = "토큰 재발급 API", description = "accessToken이 만료된 경우, refreshToken을 통해 재발급 받는 API입니다")
    public ApiResponse<LoginResponseDTO.RecreateTokenResponseDto> refreshToken(HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);
        if (token == null || token.isEmpty()) {
            throw new GeneralException(ErrorStatus.NOT_FOUND_TOKEN);
        }
        return ApiResponse.onSuccess(authService.recreateToken(token));
    }

    @PostMapping("/auth/logout")
    @Operation(summary = "로그아웃", description = "로그아웃을 하는 API입니다.")
    public ApiResponse<String> logout(HttpServletResponse response, @AuthenticationPrincipal AuthDetails authDetails) {

        if (authDetails == null || authDetails.user() == null) {
            throw new GeneralException(ErrorStatus._UNAUTHORIZED);
        }

        authService.logout(authDetails.user());
        cookieUtil.deleteCookie(response);

        return ApiResponse.onSuccess("로그아웃이 성공했습니다.");
    }
}

