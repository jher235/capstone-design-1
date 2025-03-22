package org.example.capstonedesign1.domain.propensity.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.capstonedesign1.domain.propensity.dto.response.SurveyResponse;
import org.example.capstonedesign1.domain.propensity.entity.PropensityQuestion;
import org.example.capstonedesign1.domain.propensity.repository.PropensityQuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PropensityQueryService {

    private final PropensityQuestionRepository propensityQuestionRepository;

    public SurveyResponse getSurvey(){
        List<PropensityQuestion> questions = propensityQuestionRepository.findAllWithOptions();
        return SurveyResponse.from(questions);
    }

}
