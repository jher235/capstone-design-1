package org.example.capstonedesign1.global.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class OpenAiEmbeddingRequest {
    private String input;
    private String model;
    @JsonProperty(value = "encoding_format")
    private String encodingFormat;
}
