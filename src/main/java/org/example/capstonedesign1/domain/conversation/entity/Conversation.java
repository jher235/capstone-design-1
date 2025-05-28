package org.example.capstonedesign1.domain.conversation.entity;

import org.example.capstonedesign1.domain.user.entity.User;
import org.example.capstonedesign1.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	@Column(nullable = false, length = 2000)
	private String requestMessage;

	@Column(nullable = false, length = 5000)
	private String responseMessage;

	@Column(nullable = false, length = 1000)
	private String summary;

}
