package org.example.capstonedesign1.domain.conversation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.example.capstonedesign1.domain.auth.security.CustomUserDetails;
import org.example.capstonedesign1.domain.conversation.dto.request.ConversationRequest;
import org.example.capstonedesign1.domain.conversation.dto.response.ConversationLogResponses;
import org.example.capstonedesign1.domain.conversation.dto.response.ConversationResponse;
import org.example.capstonedesign1.domain.conversation.service.ConversationCommandService;
import org.example.capstonedesign1.domain.conversation.service.ConversationQueryService;
import org.example.capstonedesign1.global.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.example.capstonedesign1.domain.conversation.constant.ConversationConstant.DEFAULT_GET_CONVERSATION_SIZE;

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
            @ApiResponse(responseCode = "5001", description = "Weaviate 관련 오류. 서버 관리자에게 문의 요망"),
            @ApiResponse(responseCode = "5002", description = "스레드 인터럽트 오류. 서버 관리자에게 문의 요망"),
            @ApiResponse(responseCode = "5003", description = "비동기 작업 중 오류. 서버 관리자에게 문의 요망")
    })
    @PostMapping
    public ResponseEntity<ResponseDto<ConversationResponse>> conversation(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "true") boolean embedding,
            @RequestBody ConversationRequest request) {

        ConversationResponse result = conversationCommandService.conversation(userDetails.getUser(), embedding, request);
        return new ResponseEntity<>(ResponseDto.res(
                HttpStatus.CREATED, "대화 요청 성공", result), HttpStatus.CREATED);
    }

    @Operation(
            summary = "llm 대화 조회 api",
            description = """
                        cursor 에 conversationId 를 전달하여 해당 conversation 이전의 conversation 목록을 가져옴
                        cursor 에 값이 오지 않는 경우 가장 최근 게시글을 가져옴.
                        정렬의 경우 최신순 이므로 조회 시 배열의 앞 요소일수록 최근의 conversation 임.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "대화 목록 조회 성공")
    })
    @GetMapping
    public ResponseEntity<ResponseDto<ConversationLogResponses>> getConversations(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(name = "cursor", required = false) UUID conversationId,
            @RequestParam(name = "size", defaultValue = DEFAULT_GET_CONVERSATION_SIZE) Integer size) {
        ConversationLogResponses response = conversationQueryService.getPreviousConversations(
                userDetails.getUser(), conversationId, size);

        return new ResponseEntity<>(ResponseDto.res(
                HttpStatus.OK, "대화 목록 조회 성공", response), HttpStatus.OK);
    }

}
