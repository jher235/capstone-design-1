package org.example.capstonedesign1.domain.cardproduct.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.example.capstonedesign1.domain.cardproduct.dto.json.RecommendedCardProduct;
import org.example.capstonedesign1.domain.cardproduct.dto.json.RecommendedCardProducts;
import org.example.capstonedesign1.domain.cardproduct.entity.CardProductRecommendation;

public record CardProductRecommendationResponse(UUID id,
												String consumptionAnalysis,
												String strategy,
												RecommendedCardProducts content,
												// String content,
												LocalDateTime createdAt) {

	public static CardProductRecommendationResponse of(
		CardProductRecommendation recommendation,
		List<RecommendedCardProduct> recommendedCardProducts
	) {
		return new CardProductRecommendationResponse(
			recommendation.getId(),
			recommendation.getConsumptionAnalysis(),
			recommendation.getStrategy(),
			new RecommendedCardProducts(recommendedCardProducts),
			recommendation.getCreatedAt());
	}

	public static CardProductRecommendationResponse from(CardProductRecommendation recommendation) {
		return new CardProductRecommendationResponse(
			recommendation.getId(),
			recommendation.getConsumptionAnalysis(),
			recommendation.getStrategy(),
			RecommendedCardProducts.fromString(recommendation.getContent()),
			// recommendation.getContent(),
			recommendation.getCreatedAt());
	}

}
