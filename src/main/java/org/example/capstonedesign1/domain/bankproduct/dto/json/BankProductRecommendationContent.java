package org.example.capstonedesign1.domain.bankproduct.dto.json;

import java.util.List;

public record BankProductRecommendationContent(List<RecommendedBankProduct> recommendations,
											   String strategy) {

}
