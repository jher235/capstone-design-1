package org.example.capstonedesign1.domain.conversation.repository;

import org.example.capstonedesign1.domain.conversation.entity.Conversation;
import org.example.capstonedesign1.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    List<Conversation> findByUser(User user, Pageable pageable);

    List<Conversation> findByUserAndCreatedAtLessThan(User user, LocalDateTime lastConversationCreated, Pageable pageable);

}
