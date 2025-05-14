package org.example.capstonedesign1.domain.conversation.dto.response;

import org.example.capstonedesign1.domain.conversation.entity.Conversation;

import java.util.List;
import java.util.UUID;

public record ConversationLogResponses(List<ConversationLogResponse> conversationLogResponses) {

    public static ConversationLogResponses from(List<Conversation> conversations) {
        return new ConversationLogResponses(conversations.stream()
                .map(ConversationLogResponse::from)
                .toList());
    }

    public record ConversationLogResponse(
            UUID id,
            String requestMessage,
            String responseMessage,
            String summary
    ) {

        public static ConversationLogResponse from(Conversation conversation) {
            return new ConversationLogResponse(
                    conversation.getId(),
                    conversation.getRequestMessage(),
                    conversation.getResponseMessage(),
                    conversation.getSummary());
        }
    }
}
