package org.example.capstonedesign1.domain.propensity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstonedesign1.domain.auth.dto.request.LoginRequest;
import org.example.capstonedesign1.domain.auth.dto.response.AuthenticationResponse;
import org.example.capstonedesign1.domain.propensity.dto.response.SurveyResponse;
import org.example.capstonedesign1.domain.propensity.service.PropensityQueryService;
import org.example.capstonedesign1.global.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/propensity")
public class PropensityController {
    private final PropensityQueryService propensityQueryService;

    @Operation(
            summary = "금융 성향 설문 문항 조회 API",
            description = "금융 성향을 판단하기 위한 질문과 응답을 조회하기 위한 api"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "문항 조회 성공")
    })
    @GetMapping
    public ResponseEntity<ResponseDto<SurveyResponse>> getQuestions(){
        SurveyResponse surveyResponse = propensityQueryService.getSurvey();

        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "문항 조회 성공", surveyResponse), HttpStatus.OK);
    }


}
