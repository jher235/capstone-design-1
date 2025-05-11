package org.example.capstonedesign1.domain.propensity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstonedesign1.domain.auth.security.CustomUserDetails;
import org.example.capstonedesign1.domain.propensity.dto.request.PropensityAnalysisRequest;
import org.example.capstonedesign1.domain.propensity.dto.response.PropensityAnalysisResponse;
import org.example.capstonedesign1.domain.propensity.dto.response.SurveyResponse;
import org.example.capstonedesign1.domain.propensity.dto.response.projection.UserPropensityPreview;
import org.example.capstonedesign1.domain.propensity.service.PropensityCommandService;
import org.example.capstonedesign1.domain.propensity.service.PropensityQueryService;
import org.example.capstonedesign1.domain.user.service.UserCommandService;
import org.example.capstonedesign1.global.dto.PaginationResponse;
import org.example.capstonedesign1.global.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/propensity")
public class PropensityController {
    private final PropensityQueryService propensityQueryService;
    private final PropensityCommandService propensityCommandService;
    private final UserCommandService userCommandService;

    @Operation(
            summary = "금융 성향 설문 문항 조회 API",
            description = "금융 성향을 판단하기 위한 질문과 응답을 조회하기 위한 api"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "문항 조회 성공")
    })
    @GetMapping
    public ResponseEntity<ResponseDto<SurveyResponse>> getQuestions() {
        SurveyResponse surveyResponse = propensityQueryService.getSurvey();

        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "문항 조회 성공", surveyResponse), HttpStatus.OK);
    }

    @Operation(
            summary = "금융 성향 설문 제출 API",
            description = "금융 성향을 판단하기 위한 응답 요청하는 api"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "금융 성향 분석 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부에서 json 파싱 오류")
    })
    @PostMapping
    public ResponseEntity<ResponseDto<PropensityAnalysisResponse>> postSurvey(@Valid @RequestBody PropensityAnalysisRequest request,
                                                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        PropensityAnalysisResponse response = propensityCommandService.submitSurvey(userDetails.getUser(), request);

        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "금융 성향 분석 성공", response), HttpStatus.OK);
    }

    @Operation(
            summary = "금융 성향 분석 목록 조회 API",
            description = "금융 성향 분석 목록을 조회하는 api"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "금융 성향 분석 목록 조회 성공"),
    })
    @GetMapping("/analysis")
    public ResponseEntity<ResponseDto<PaginationResponse<UserPropensityPreview>>> getUserPropensities(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        PaginationResponse<UserPropensityPreview> response = propensityQueryService.getUserPropensities(userDetails.getUser(), page, size);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "금융 성향 분석 목록 조회 성공", response), HttpStatus.OK);
    }

    @Operation(
            summary = "금융 성향 분석 상세 조회 API",
            description = "기존에 받았던 금융 성향 분석 결과를 상세 조회하는 api"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "금융 성향 분석 상세 조회 성공"),
            @ApiResponse(responseCode = "4041", description = "요청한 금융 성향 분석을 찾을 수 없는 경우"),
            @ApiResponse(responseCode = "4030", description = "요청한 금융 성향 분석이 사용자의 분석 결과가 아닌 경우"),
    })
    @GetMapping("/analysis/{userPropensityId}")
    public ResponseEntity<ResponseDto<PropensityAnalysisResponse>> getUserPropensity(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(name = "userPropensityId") UUID userPropensityId) {
        PropensityAnalysisResponse response = propensityQueryService.getUserPropensity(userDetails.getUser(), userPropensityId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "금융 성향 분석 상세 조회 성공", response), HttpStatus.OK);
    }


}
