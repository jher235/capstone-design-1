package org.example.capstonedesign1.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstonedesign1.domain.auth.dto.request.LoginRequest;
import org.example.capstonedesign1.domain.auth.dto.request.SignUpCompleteRequest;
import org.example.capstonedesign1.domain.auth.dto.request.SignUpRequest;
import org.example.capstonedesign1.domain.auth.dto.response.AuthenticationResponse;
import org.example.capstonedesign1.domain.auth.security.CustomUserDetails;
import org.example.capstonedesign1.domain.auth.service.AuthCommandService;
import org.example.capstonedesign1.global.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthCommandService authCommandService;

    @Operation(
            summary = "로컬 회원가입 API",
            description = "이메일과 비밀번호를 사용하여 새로운 회원을 등록"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "4090", description = "이메일 중복")
    })
    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDto<AuthenticationResponse>> signUp(@RequestBody @Valid SignUpRequest request){
        AuthenticationResponse response = authCommandService.signUp(request);

        return new ResponseEntity<>(
                ResponseDto.res(HttpStatus.CREATED, "회원가입 성공", response), HttpStatus.CREATED);
    }

    @Operation(
            summary = "회원가입 완료 API",
            description = "프로필 정보를 입력받아 회원가입 완료"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 완료 성공"),
            @ApiResponse(responseCode = "4091", description = "이미 로그인 완료한 사용자인 경우")
    })
    @PostMapping("/sign-up/complete")
    public ResponseEntity<ResponseDto<Void>> SignUpComplete(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                            @RequestBody @Valid SignUpCompleteRequest request){
        authCommandService.signUpComplete(customUserDetails.getUser(), request);
        return new ResponseEntity<>(
                ResponseDto.res(HttpStatus.CREATED, "회원가입 완료 성공"), HttpStatus.OK);
    }

    @Operation(
            summary = "로그인 API",
            description = "이메일과 비밀번호를 사용하여 로그인"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "4010", description = "로그인 실패"),
    })
    @PostMapping("/login")
    public ResponseEntity<ResponseDto<Void>> login(@RequestBody @Valid LoginRequest request){

        return new ResponseEntity<>(
                ResponseDto.res(HttpStatus.CREATED, "로그인 성공"), HttpStatus.CREATED);
    }



}
