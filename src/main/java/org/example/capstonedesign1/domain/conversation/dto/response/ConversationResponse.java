package org.example.capstonedesign1.domain.conversation.dto.response;

import org.example.capstonedesign1.domain.conversation.entity.Conversation;

public record ConversationResponse(String responseMessage,
                                   String summary) {

    public static ConversationResponse from(Conversation conversation) {
        return new ConversationResponse(conversation.getResponseMessage(), conversation.getSummary());
    }
}
