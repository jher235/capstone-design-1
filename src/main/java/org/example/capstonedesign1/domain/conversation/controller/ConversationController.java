package org.example.capstonedesign1.domain.conversation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.example.capstonedesign1.domain.auth.security.CustomUserDetails;
import org.example.capstonedesign1.domain.conversation.dto.request.ConversationRequest;
import org.example.capstonedesign1.domain.conversation.dto.response.ConversationResponse;
import org.example.capstonedesign1.domain.conversation.service.ConversationCommandService;
import org.example.capstonedesign1.domain.conversation.service.ConversationQueryService;
import org.example.capstonedesign1.global.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/conversations")
public class ConversationController {
    private final ConversationQueryService conversationQueryService;
    private final ConversationCommandService conversationCommandService;


    @Operation(
            summary = "llm 대화 요청 api",
            description = "llm 과 대화를 진행하는 api. 사용자는 vector 쿼리 스트링으로 텍스트 임베딩을 통한 RAG 기술 사용이 가능"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "대화 요청 성공"),
            @ApiResponse(responseCode = "5001", description = "Weaviate 관련 오류. 서버 관리자에게 문의 요망")
    })
    @PostMapping()
    public ResponseEntity<ResponseDto<ConversationResponse>> recommendBankProducts(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "true") boolean embedding,
            @RequestBody ConversationRequest request) {

        ConversationResponse result = conversationCommandService.conversation(userDetails.getUser(), embedding, request);
        return new ResponseEntity<>(ResponseDto.res(
                HttpStatus.CREATED, "대화 요청 성공", result), HttpStatus.CREATED);
    }


}
