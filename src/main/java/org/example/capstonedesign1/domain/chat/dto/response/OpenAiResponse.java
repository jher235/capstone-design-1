package org.example.capstonedesign1.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.capstonedesign1.domain.chat.dto.Message;

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
