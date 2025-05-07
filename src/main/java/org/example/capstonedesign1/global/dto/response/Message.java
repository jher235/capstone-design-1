package org.example.capstonedesign1.global.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String role; // "system", "user", "assistant"
    private String content;
}
