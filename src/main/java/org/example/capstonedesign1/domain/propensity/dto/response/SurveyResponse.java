package org.example.capstonedesign1.domain.propensity.dto.response;

import org.example.capstonedesign1.domain.propensity.entity.PropensityQuestion;

import java.util.List;

public record SurveyResponse(List<SurveyItemResponse> surveyItemResponseList) {
    public static SurveyResponse from(List<PropensityQuestion> questions){
        return new SurveyResponse(questions.stream()
                .map(SurveyItemResponse::from)
                .toList());
    }
}
