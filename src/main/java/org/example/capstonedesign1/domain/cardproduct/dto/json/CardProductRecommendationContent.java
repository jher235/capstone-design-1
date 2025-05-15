package org.example.capstonedesign1.domain.cardproduct.dto.json;

import java.util.List;

import org.example.capstonedesign1.domain.bankproduct.dto.json.BankProductRecommendationContent;

public record CardProductRecommendationContent(
	List<BankProductRecommendationContent.RecommendedProductContent> recommendations,
	String strategy) {

	public record RecommendedProductContent(String id,
											String description,
											String reason,
											String detailUrl,
											String imageUrl) {
	}
}
