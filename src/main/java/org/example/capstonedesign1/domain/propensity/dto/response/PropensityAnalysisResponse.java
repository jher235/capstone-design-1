package org.example.capstonedesign1.domain.propensity.dto.response;


import org.example.capstonedesign1.domain.propensity.dto.json.PropensityAnalysis;

import java.time.LocalDateTime;
import java.util.UUID;

public record PropensityAnalysisResponse(UUID id,
                                         PropensityAnalysis propensityAnalysis,
                                         LocalDateTime createdAt) {
}
