package org.example.capstonedesign1.domain.conversation.dto.response;

import org.example.capstonedesign1.domain.conversation.entity.Conversation;

import java.util.UUID;

public record ConversationResponse(UUID conversationId,
                                   String responseMessage,
                                   String summary) {

    public static ConversationResponse from(Conversation conversation) {
        return new ConversationResponse(conversation.getId(), conversation.getResponseMessage(), conversation.getSummary());
    }
}
