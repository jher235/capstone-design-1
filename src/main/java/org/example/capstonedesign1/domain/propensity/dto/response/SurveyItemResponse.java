package org.example.capstonedesign1.domain.propensity.dto.response;


import org.example.capstonedesign1.domain.propensity.entity.PropensityQuestion;
import org.example.capstonedesign1.domain.propensity.entity.PropensityQuestionOption;

import java.util.List;

public record SurveyItemResponse(String question, List<String> option) {
    public static SurveyItemResponse from(PropensityQuestion question){
        return new SurveyItemResponse(question.getContent(),
                question.getOptions().stream()
                        .map(PropensityQuestionOption::getContent)
                        .toList());
    }
}
