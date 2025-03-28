package org.example.capstonedesign1.domain.bankproduct.dto.response;


import org.example.capstonedesign1.domain.bankproduct.dto.json.BankProductRecommendationContent;

import java.time.LocalDateTime;
import java.util.UUID;

public record BankProductRecommendationResponse (UUID id,
                                                 BankProductRecommendationContent content,
                                                 LocalDateTime createdAt
                                                 ){

}
