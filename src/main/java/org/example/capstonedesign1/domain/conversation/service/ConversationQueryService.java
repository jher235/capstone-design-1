package org.example.capstonedesign1.domain.conversation.service;

import lombok.RequiredArgsConstructor;
import org.example.capstonedesign1.domain.conversation.repository.ConversationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConversationQueryService {
    private final ConversationRepository conversationRepository;
}
