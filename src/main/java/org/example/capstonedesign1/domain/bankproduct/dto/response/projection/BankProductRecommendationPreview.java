package org.example.capstonedesign1.domain.bankproduct.dto.response.projection;

import java.time.LocalDateTime;
import java.util.UUID;

public interface BankProductRecommendationPreview {
    UUID getId();
    String getStrategy();
    LocalDateTime getCreatedAt();
}
