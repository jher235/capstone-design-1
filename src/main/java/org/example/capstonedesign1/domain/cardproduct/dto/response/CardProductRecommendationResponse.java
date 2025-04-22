package org.example.capstonedesign1.domain.cardproduct.dto.response;

import org.example.capstonedesign1.domain.cardproduct.entity.CardProductRecommendation;

import java.time.LocalDateTime;
import java.util.UUID;

public record CardProductRecommendationResponse(UUID id,
                                                String strategy,
                                                String content,
                                                LocalDateTime createdAt) {

    public static CardProductRecommendationResponse from(CardProductRecommendation recommendation) {
        return new CardProductRecommendationResponse(
                recommendation.getId(), recommendation.getStrategy(), recommendation.getContent(), recommendation.getCreatedAt());
    }

}
