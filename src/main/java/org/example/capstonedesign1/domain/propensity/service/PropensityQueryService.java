package org.example.capstonedesign1.domain.propensity.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.capstonedesign1.domain.propensity.dto.response.SurveyResponse;
import org.example.capstonedesign1.domain.propensity.entity.PropensityQuestion;
import org.example.capstonedesign1.domain.propensity.entity.UserPropensity;
import org.example.capstonedesign1.domain.propensity.repository.PropensityQuestionRepository;
import org.example.capstonedesign1.domain.propensity.repository.UserPropensityRepository;
import org.example.capstonedesign1.global.exception.NotFoundException;
import org.example.capstonedesign1.global.exception.code.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PropensityQueryService {

    private final PropensityQuestionRepository propensityQuestionRepository;
    private final UserPropensityRepository userPropensityRepository;

    public SurveyResponse getSurvey(){
        List<PropensityQuestion> questions = propensityQuestionRepository.findAllWithOptions();
        return SurveyResponse.from(questions);
    }

    public UserPropensity findUserPropensityById(UUID userPropensityId){
        return userPropensityRepository.findById(userPropensityId).orElseThrow(()
                -> new NotFoundException(ErrorCode.USER_PROPENSITY_NOT_FOUND));
    }

}
