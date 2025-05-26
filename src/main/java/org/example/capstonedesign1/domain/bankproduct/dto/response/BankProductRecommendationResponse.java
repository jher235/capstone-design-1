package org.example.capstonedesign1.domain.bankproduct.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import org.example.capstonedesign1.domain.bankproduct.dto.json.BankProductRecommendationContent;
import org.example.capstonedesign1.domain.bankproduct.dto.json.RecommendedBankProducts;
import org.example.capstonedesign1.domain.bankproduct.entity.BankProductRecommendation;

public record BankProductRecommendationResponse(UUID id,
												String strategy,
												RecommendedBankProducts content,
												LocalDateTime createdAt
) {

	public static BankProductRecommendationResponse of(UUID id, LocalDateTime createdAt,
		BankProductRecommendationContent content) {
		return new BankProductRecommendationResponse(
			id,
			content.strategy(),
			new RecommendedBankProducts(content.recommendations()),
			createdAt);
	}

	public static BankProductRecommendationResponse from(BankProductRecommendation recommendation) {
		return new BankProductRecommendationResponse(
			recommendation.getId(),
			recommendation.getStrategy(),
			RecommendedBankProducts.fromString(recommendation.getContent()),
			recommendation.getCreatedAt());
	}
}
