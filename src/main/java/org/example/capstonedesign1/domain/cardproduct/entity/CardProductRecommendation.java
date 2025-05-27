package org.example.capstonedesign1.domain.cardproduct.entity;

import org.example.capstonedesign1.domain.user.entity.User;
import org.example.capstonedesign1.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardProductRecommendation extends BaseEntity {
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	User user;

	@Column(length = 1000)
	private String strategy;

	@Column(nullable = false, columnDefinition = "JSON")
	private String content;

	@Column(length = 1000)
	private String consumptionAnalysis;

	@Builder
	private CardProductRecommendation(User user, String strategy, String content, String consumptionAnalysis) {
		this.user = user;
		this.strategy = strategy;
		this.content = content;
		this.consumptionAnalysis = consumptionAnalysis;
	}
}
