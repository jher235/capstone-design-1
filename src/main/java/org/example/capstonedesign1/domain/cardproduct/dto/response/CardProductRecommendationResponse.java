package org.example.capstonedesign1.domain.cardproduct.dto.response;

import org.example.capstonedesign1.domain.cardproduct.dto.json.CardProductRecommendationContent;

import java.time.LocalDateTime;
import java.util.UUID;

public record CardProductRecommendationResponse (UUID id,
                                                 CardProductRecommendationContent content,
                                                 LocalDateTime createdAt){

}
