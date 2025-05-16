package org.example.capstonedesign1.domain.cardproduct.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import org.example.capstonedesign1.domain.cardproduct.entity.CardProductRecommendation;

public record CardProductRecommendationResponse(UUID id,
												String consumptionAnalysis,
												String strategy,
												String content,
												LocalDateTime createdAt) {

	public static CardProductRecommendationResponse from(CardProductRecommendation recommendation) {
		return new CardProductRecommendationResponse(
			recommendation.getId(),
			recommendation.getConsumptionAnalysis(),
			recommendation.getStrategy(),
			recommendation.getContent(),
			recommendation.getCreatedAt());
	}

}
