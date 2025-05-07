package org.example.capstonedesign1.global.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OpenAiEmbeddingResponse {
    private String object;
    private List<Data> data;
    private String model;
    private Usage usage;

    @Getter
    @NoArgsConstructor
    public static class Data {
        private String object;
        private int index;
        private Float[] embedding;
    }

    @Getter
    @NoArgsConstructor
    public static class Usage {
        @JsonProperty(value = "prompt_tokens")
        int promptTokens;
        @JsonProperty(value = "total_tokens")
        int totalTokens;
    }
}
