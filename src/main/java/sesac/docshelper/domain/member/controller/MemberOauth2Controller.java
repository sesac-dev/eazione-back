package sesac.docshelper.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sesac.docshelper.domain.member.dto.response.SignUpResponse;
import sesac.docshelper.domain.member.service.MemberOAuth2Service;
import sesac.docshelper.global.dto.response.ResultResponse;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberOauth2Controller {
    private final MemberOAuth2Service memberOAuth2Service;

    @Value("${oauth2.google.redirect-uri.local}")
    private String localRedirectUri;
    @Value("${oauth2.google.redirect-uri.dev}")
    private String deployRedirectUri;

    @Operation(summary = "Front Local에서 소셜 로그인 요청 보낼 때")
    @GetMapping("/oauth/google/signup/l")
    public ResponseEntity<ResultResponse<SignUpResponse>> signUpLocal (@RequestParam(value = "code") String authorization_code) {
       return ResponseEntity.ok(ResultResponse.success(memberOAuth2Service.signUp(authorization_code, localRedirectUri)));
    }

    @Operation(summary = "Front 배포에서 소셜 로그인 요청 보낼 때")
    @GetMapping("/oauth/google/signup/d")
    public ResponseEntity<ResultResponse<SignUpResponse>> signUpDeploy (@RequestParam(value = "code") String authorization_code) {
        return ResponseEntity.ok(ResultResponse.success(memberOAuth2Service.signUp(authorization_code, deployRedirectUri)));
    }

}
