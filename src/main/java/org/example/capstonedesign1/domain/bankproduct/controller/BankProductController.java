package org.example.capstonedesign1.domain.bankproduct.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstonedesign1.domain.auth.security.CustomUserDetails;
import org.example.capstonedesign1.domain.bankproduct.dto.request.BankProductRecommendRequest;
import org.example.capstonedesign1.domain.bankproduct.dto.response.BankProductRecommendationResponse;
import org.example.capstonedesign1.domain.bankproduct.service.BankProductCommandService;
import org.example.capstonedesign1.global.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/bank-product")
public class BankProductController {
    private final BankProductCommandService bankProductCommandService;

    @Operation(
            summary = "금융 상품 추천 API",
            description = "입력받은 금액과 기간, 그리고 유저의 상세 정보를 사용하여 금융 상품 추천을 받는 api"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "금융 상품 추천 받기 성공"),
            @ApiResponse(responseCode = "4007", description = "아직 금융 성향 분석을 진행하지 않은 경우"),
            @ApiResponse(responseCode = "5000", description = "서버 내부에서 json 파싱 오류")
    })
    @PostMapping("/recommend")
    public ResponseEntity<ResponseDto<BankProductRecommendationResponse>> getUserPropensity(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid BankProductRecommendRequest request){
        BankProductRecommendationResponse recommendationResponse =  bankProductCommandService.recommendBankProduct(userDetails.getUser(), request);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "금융 상품 추천 받기 성공", recommendationResponse), HttpStatus.OK);
    }


}
