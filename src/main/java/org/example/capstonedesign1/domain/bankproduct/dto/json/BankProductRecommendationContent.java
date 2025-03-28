package org.example.capstonedesign1.domain.bankproduct.dto.json;

import java.util.List;

public record BankProductRecommendationContent(List<RecommendedProductContent> recommendations,
                                               String strategy) {

    public record RecommendedProductContent (String id,
                                             String description,
                                             String reason,
                                             String detailUrl) {
    }

}
