package org.example.capstonedesign1.domain.cardproduct.controller;

import java.util.Optional;
import java.util.UUID;

import org.example.capstonedesign1.domain.auth.security.CustomUserDetails;
import org.example.capstonedesign1.domain.cardproduct.dto.response.CardProductRecommendationResponse;
import org.example.capstonedesign1.domain.cardproduct.dto.response.projection.CardProductRecommendationPreview;
import org.example.capstonedesign1.domain.cardproduct.service.CardProductCommandService;
import org.example.capstonedesign1.domain.cardproduct.service.CardProductQueryService;
import org.example.capstonedesign1.global.dto.PaginationResponse;
import org.example.capstonedesign1.global.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/card-products")
public class CardProductController {

	private final CardProductCommandService cardProductCommandService;
	private final CardProductQueryService cardProductQueryService;

	@Operation(
		summary = "카드 상품 추천 API",
		description = "입력받은 결제 내역, 그리고 유저의 상세 정보를 사용하여 카드 상품을 추천받는 api"
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "금융 상품 추천 받기 성공"),
		@ApiResponse(responseCode = "4007", description = "아직 금융 성향 분석을 진행하지 않은 경우"),
		@ApiResponse(responseCode = "5000", description = "서버 내부에서 json 파싱 오류")
	})
	@PostMapping(value = "/recommendations", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ResponseDto<CardProductRecommendationResponse>> getUserPropensity(
		@RequestPart("file") MultipartFile file,
		@RequestPart(value = "password", required = false) String filePassword,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		CardProductRecommendationResponse response = cardProductCommandService.recommendCardProduct(
			userDetails.getUser(), file, Optional.ofNullable(filePassword));
		return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "카드 상품 추천 받기 성공", response), HttpStatus.OK);
	}

	@Operation(
		summary = "카드 상품 추천 목록 조회 API",
		description = "추천 받은 카드 상품 목록을 조회하는 api"
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "카드 상품 추천 결과 목록 조회 성공"),
	})
	@GetMapping("/recommendations")
	public ResponseEntity<ResponseDto<PaginationResponse<CardProductRecommendationPreview>>> getRecommendationPreviews(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam(name = "page", defaultValue = "0") int page,
		@RequestParam(name = "size", defaultValue = "10") int size) {
		PaginationResponse<CardProductRecommendationPreview> recommendationsPreview
			= cardProductQueryService.getRecommendations(userDetails.getUser(), page, size);
		return new ResponseEntity<>(ResponseDto.res(
			HttpStatus.OK, "카드 상품 추천 목록 조회 성공", recommendationsPreview), HttpStatus.OK);
	}

	@Operation(
		summary = "카드 상품 추천 상세 조회 API",
		description = "추천 받은 카드 상품 내역을 상세 조회하는 api"
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "카드 상품 추천 결과 상세 조회 성공"),
		@ApiResponse(responseCode = "4042", description = "요청한 카드 성향 분석을 찾을 수 없는 경우"),
		@ApiResponse(responseCode = "4030", description = "요청한 카드 상품 추천이 사용자의 추천 결과가 아닌 경우")
	})
	@GetMapping("/recommendations/{recommendation-id}")
	public ResponseEntity<ResponseDto<CardProductRecommendationResponse>> getRecommendation(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(name = "recommendation-id") UUID recommendationId) {
		CardProductRecommendationResponse response
			= cardProductQueryService.getRecommendation(userDetails.getUser(), recommendationId);
		return new ResponseEntity<>(ResponseDto.res(
			HttpStatus.OK, "카드 상품 추천 목록 조회 성공", response), HttpStatus.OK);
	}
}
