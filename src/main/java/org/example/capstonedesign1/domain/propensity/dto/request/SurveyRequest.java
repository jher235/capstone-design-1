package org.example.capstonedesign1.domain.propensity.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SurveyRequest {
    @NotNull(message = "설문 항목은 필수입니다.")
    private List<SurveyItemEntry> surveyEntries;

    @Getter
    @NoArgsConstructor
    public static class SurveyItemEntry{
        @NotEmpty(message = "질문은 비어있을 수 없습니다.")
        private String question;
        @NotEmpty(message = "답변은 비어있을 수 없습니다.")
        private String selectedAnswer;
    }
}
