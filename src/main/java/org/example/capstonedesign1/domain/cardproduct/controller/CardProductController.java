package org.example.capstonedesign1.domain.cardproduct.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.example.capstonedesign1.domain.auth.security.CustomUserDetails;
import org.example.capstonedesign1.domain.cardproduct.dto.response.CardProductRecommendationResponse;
import org.example.capstonedesign1.domain.cardproduct.service.CardProductCommandService;
import org.example.capstonedesign1.global.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/card-product")
public class CardProductController {

    private final CardProductCommandService cardProductCommandService;

    @Operation(
            summary = "카드 상품 추천 API",
            description = "입력받은 결제 내역, 그리고 유저의 상세 정보를 사용하여 카드 상품을 추천받는 api"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "금융 상품 추천 받기 성공"),
            @ApiResponse(responseCode = "4007", description = "아직 금융 성향 분석을 진행하지 않은 경우"),
            @ApiResponse(responseCode = "5000", description = "서버 내부에서 json 파싱 오류")
    })
    @PostMapping(value = "/recommend", consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<ResponseDto<CardProductRecommendationResponse>> getUserPropensity(
            @RequestPart MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails userDetails){
        CardProductRecommendationResponse response = cardProductCommandService.recommendCardProduct(userDetails.getUser(), file);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "카드 상품 추천 받기 성공", response), HttpStatus.OK);
    }
}
