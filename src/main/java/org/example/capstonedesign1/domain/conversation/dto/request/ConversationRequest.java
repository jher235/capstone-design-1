package org.example.capstonedesign1.domain.conversation.dto.request;

import org.hibernate.validator.constraints.Length;

public record ConversationRequest(
        @Length(max = 1000, min = 1, message = "메세지는 {min}자 이상, {max}이하여야 합니다.") String requestMessage,
        String summary) {

}
