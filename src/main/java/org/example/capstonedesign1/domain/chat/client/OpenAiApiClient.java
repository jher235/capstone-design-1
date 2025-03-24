package org.example.capstonedesign1.domain.chat.client;

import lombok.extern.slf4j.Slf4j;
import org.example.capstonedesign1.domain.chat.dto.Message;
import org.example.capstonedesign1.domain.chat.dto.request.OpenAiRequest;
import org.example.capstonedesign1.domain.chat.dto.response.OpenAiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Component
public class OpenAiApiClient {
    public final static String SYSTEM_ROLE = "system";
    public final static String REQUEST_URI = "/chat/completions";

    private final static String BEARER_TYPE = "Bearer ";
    private final WebClient webClient;
    @Value("${openai.model}")
    private String model;
    @Value("${openai.max-tokens}")
    private int maxTokens;
    @Value("${openai.temperature}")
    private double temperature;


    public OpenAiApiClient(WebClient.Builder webClientBuilder,
                           @Value("${openai.secret-key}") String secretKey,
                           @Value("${openai.base-url}") String baseUrl){
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, BEARER_TYPE + secretKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public OpenAiResponse sendRequest(List<Message> messages) {
        OpenAiRequest request = new OpenAiRequest(model, messages, maxTokens, temperature);
        log.info("request: {}", request);

        return webClient.post()
                .uri(REQUEST_URI)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(OpenAiResponse.class)
                .block();
    }
}
