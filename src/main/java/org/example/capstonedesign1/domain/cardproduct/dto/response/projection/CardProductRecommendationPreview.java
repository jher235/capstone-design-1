package org.example.capstonedesign1.domain.cardproduct.dto.response.projection;

import java.time.LocalDateTime;
import java.util.UUID;

public interface CardProductRecommendationPreview {
    UUID getId();

    String getStrategy();

    LocalDateTime getCreatedAt();
}
