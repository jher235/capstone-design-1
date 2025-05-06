package org.example.capstonedesign1.global.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.capstonedesign1.global.dto.response.Message;

import java.util.List;

@Getter
@AllArgsConstructor
public class OpenAiRequest {
    private String model;
    private List<Message> messages;
    private int max_tokens;
    private double temperature;
}
