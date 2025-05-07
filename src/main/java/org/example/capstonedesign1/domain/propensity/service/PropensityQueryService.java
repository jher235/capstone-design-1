package org.example.capstonedesign1.domain.propensity.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.capstonedesign1.domain.propensity.dto.json.PropensityAnalysis;
import org.example.capstonedesign1.domain.propensity.dto.response.PropensityAnalysisResponse;
import org.example.capstonedesign1.domain.propensity.dto.response.SurveyResponse;
import org.example.capstonedesign1.domain.propensity.dto.response.projection.UserPropensityPreview;
import org.example.capstonedesign1.domain.propensity.entity.PropensityQuestion;
import org.example.capstonedesign1.domain.propensity.entity.UserPropensity;
import org.example.capstonedesign1.domain.propensity.repository.PropensityQuestionRepository;
import org.example.capstonedesign1.domain.propensity.repository.UserPropensityRepository;
import org.example.capstonedesign1.domain.user.entity.User;
import org.example.capstonedesign1.domain.user.service.UserQueryService;
import org.example.capstonedesign1.global.dto.PaginationResponse;
import org.example.capstonedesign1.global.exception.ForbiddenException;
import org.example.capstonedesign1.global.exception.NotFoundException;
import org.example.capstonedesign1.global.exception.code.ErrorCode;
import org.example.capstonedesign1.global.util.JsonUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PropensityQueryService {

    private final PropensityQuestionRepository propensityQuestionRepository;
    private final UserPropensityRepository userPropensityRepository;
    private final UserQueryService userQueryService;


    public SurveyResponse getSurvey() {
        List<PropensityQuestion> questions = propensityQuestionRepository.findAllWithOptions();
        return SurveyResponse.from(questions);
    }

    public UserPropensity findUserPropensityById(UUID userPropensityId) {
        return userPropensityRepository.findById(userPropensityId).orElseThrow(()
                -> new NotFoundException(ErrorCode.USER_PROPENSITY_NOT_FOUND));
    }

    public PaginationResponse<UserPropensityPreview> getUserPropensities(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<UserPropensityPreview> propensityPreviews =
                userPropensityRepository.findUserPropensityPreviews(user, pageable);

        return PaginationResponse.from(propensityPreviews);
    }

    public PropensityAnalysisResponse getUserPropensity(User user, UUID userPropensityId) {
        UserPropensity userPropensity = findUserPropensityById(userPropensityId);
        if (!userQueryService.isSameUser(user, userPropensity.getUser())) {
            throw new ForbiddenException(ErrorCode.UN_AUTHORIZED);
        }

        PropensityAnalysis propensityAnalysis = JsonUtil.parseClass(
                PropensityAnalysis.class, userPropensity.getContent());

        return new PropensityAnalysisResponse(userPropensity.getId(), propensityAnalysis, userPropensity.getCreatedAt());
    }

}
