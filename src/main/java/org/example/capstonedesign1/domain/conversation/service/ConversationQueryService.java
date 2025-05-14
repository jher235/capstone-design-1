package org.example.capstonedesign1.domain.conversation.service;

import lombok.RequiredArgsConstructor;
import org.example.capstonedesign1.domain.conversation.dto.response.ConversationLogResponses;
import org.example.capstonedesign1.domain.conversation.entity.Conversation;
import org.example.capstonedesign1.domain.conversation.repository.ConversationRepository;
import org.example.capstonedesign1.domain.user.entity.User;
import org.example.capstonedesign1.global.exception.NotFoundException;
import org.example.capstonedesign1.global.exception.code.ErrorCode;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConversationQueryService {
    private final ConversationRepository conversationRepository;

    public Conversation findById(UUID conversationId) {
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CONVERSATION_NOT_FOUND));
    }

    public ConversationLogResponses getPreviousConversations(User user, UUID conversationId, Integer size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by("createdAt").descending());

        if (conversationId != null) {
            Conversation conversation = findById(conversationId);
            List<Conversation> conversations = conversationRepository.findByUserAndCreatedAtLessThan(user, conversation.getCreatedAt(), pageable);
            return ConversationLogResponses.from(conversations);
        }

        List<Conversation> conversations = conversationRepository.findByUser(user, pageable);
        return ConversationLogResponses.from(conversations);
    }

}
