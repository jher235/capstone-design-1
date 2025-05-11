package org.example.capstonedesign1.domain.propensity.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record PropensityAnalysisRequest(
        @Schema(description = "유저 성향 분석 결과",
                example = """
                        균형형: 10,
                        공격형: 20,
                        저축형: 70
                        소비형: 0,
                        융합형: 0
                        """)
        @NotNull String userSurveyResult) {
}
