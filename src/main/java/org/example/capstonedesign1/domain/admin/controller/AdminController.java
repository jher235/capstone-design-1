package org.example.capstonedesign1.domain.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.example.capstonedesign1.domain.admin.service.AdminCommandService;
import org.example.capstonedesign1.domain.auth.security.CustomUserDetails;
import org.example.capstonedesign1.global.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminCommandService adminCommandService;

    @Operation(
            summary = "Weaviate에 스키마 작성 ",
            description = "테스트를 위해 임시로 동작을 넣음"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스키마 생성 성공")
    })
    @PostMapping("/weaviate/schema")
    public ResponseEntity<ResponseDto<Void>> createSchema(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        adminCommandService.createSchema(userDetails.getUser());
        return new ResponseEntity<>(
                ResponseDto.res(HttpStatus.CREATED, "대화 요청 성공"), HttpStatus.CREATED);
    }

    @Operation(
            summary = "DB 데이터로 Weaviate 데이터 세팅",
            description = "테스트를 위해 임의로 작성함. 아직 동작하지 않음"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "데이터 세팅 성공")
    })
    @PostMapping("/weaviate/set")
    public ResponseEntity<ResponseDto<Void>> set(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        adminCommandService.setData(userDetails.getUser());
        return new ResponseEntity<>(
                ResponseDto.res(HttpStatus.OK, "데이터 세팅 성공"), HttpStatus.OK);
    }


}
