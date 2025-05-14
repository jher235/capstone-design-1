package org.example.capstonedesign1.domain.conversation.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.capstonedesign1.domain.user.entity.User;
import org.example.capstonedesign1.global.common.BaseEntity;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "idx_user_createdat", columnList = "user_id, created_at")
})
public class Conversation extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String requestMessage;

    private String responseMessage;

    private String summary;

}
