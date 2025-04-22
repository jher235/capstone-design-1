package org.example.capstonedesign1.domain.bankproduct.dto.response;


import org.example.capstonedesign1.domain.bankproduct.entity.BankProductRecommendation;

import java.time.LocalDateTime;
import java.util.UUID;

public record BankProductRecommendationResponse(UUID id,
                                                String strategy,
                                                String content,
                                                LocalDateTime createdAt
) {
    public static BankProductRecommendationResponse from(BankProductRecommendation recommendation) {
        return new BankProductRecommendationResponse(
                recommendation.getId(), recommendation.getStrategy(), recommendation.getContent(), recommendation.getCreatedAt());
    }
}
