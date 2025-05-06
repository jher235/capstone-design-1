package org.example.capstonedesign1.global.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OpenAiResponse {

    private List<Choice> choices;

    @Getter
    @NoArgsConstructor
    public static class Choice {
        private int index;
        private Message message;
    }
}
