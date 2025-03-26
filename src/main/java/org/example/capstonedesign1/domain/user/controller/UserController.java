package org.example.capstonedesign1.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.example.capstonedesign1.domain.auth.security.CustomUserDetails;
import org.example.capstonedesign1.domain.user.service.UserCommandService;
import org.example.capstonedesign1.global.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserCommandService userCommandService;


    @Operation(
            summary = "금융 성향 업데이트 API",
            description = "유저의 금융 성향을 업데이트하는 api"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "금융 성향 업데이트 성공"),
            @ApiResponse(responseCode = "4041", description = "요청한 금융 성향 분석을 찾을 수 없는 경우"),
            @ApiResponse(responseCode = "4030", description = "요청한 금융 성향 분석이 사용자의 분석 결과가 아닌 경우"),
    })
    @PatchMapping("/propensity/{userPropensityId}")
    public ResponseEntity<ResponseDto<Void>> postSurvey(@PathVariable UUID userPropensityId,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails){
        userCommandService.updatePropensity(userDetails.getUser(), userPropensityId);

        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "금융 성향 업데이트 성공"), HttpStatus.OK);
    }
}
