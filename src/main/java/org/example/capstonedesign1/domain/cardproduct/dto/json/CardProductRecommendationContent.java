package org.example.capstonedesign1.domain.cardproduct.dto.json;

import java.util.List;

public record CardProductRecommendationContent(List<RecommendedCardProduct> recommendations,
											   String strategy) {

}
