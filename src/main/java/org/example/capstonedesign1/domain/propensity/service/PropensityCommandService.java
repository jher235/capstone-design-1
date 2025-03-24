package org.example.capstonedesign1.domain.propensity.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.capstonedesign1.domain.chat.client.OpenAiApiClient;
import org.example.capstonedesign1.domain.chat.dto.Message;
import org.example.capstonedesign1.domain.chat.dto.response.OpenAiResponse;
import org.example.capstonedesign1.domain.propensity.dto.json.PropensityAnalysis;
import org.example.capstonedesign1.domain.propensity.dto.request.SurveyRequest;
import org.example.capstonedesign1.domain.propensity.dto.response.PropensityAnalysisResponse;
import org.example.capstonedesign1.domain.propensity.entity.UserPropensity;
import org.example.capstonedesign1.domain.propensity.repository.UserPropensityRepository;
import org.example.capstonedesign1.domain.user.entity.User;
import org.example.capstonedesign1.global.template.PromptTemplate;
import org.example.capstonedesign1.global.util.JsonUtil;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.capstonedesign1.domain.chat.client.OpenAiApiClient.SYSTEM_ROLE;

@Slf4j
@Service
@RequiredArgsConstructor
public class PropensityCommandService {

    private final OpenAiApiClient openAiApiClient;

    private final UserPropensityRepository userPropensityRepository;

    public PropensityAnalysisResponse submitSurvey(User user, SurveyRequest request){
        String requestMessage =  PromptTemplate.propensityAnalysisPrompt(
                user.getProfile().getGender().toString(),
                request.getSurveyEntries());

        OpenAiResponse response =  openAiApiClient.sendRequest(List.of(new Message(SYSTEM_ROLE, requestMessage)) );

        String content = response.getChoices().get(0).getMessage().getContent().trim();

        PropensityAnalysis propensityAnalysis = JsonUtil.parseClass(
                PropensityAnalysis.class, content);
        UserPropensity userPropensity = new UserPropensity(user, propensityAnalysis.type(), content);
        userPropensityRepository.save(userPropensity);
        return new PropensityAnalysisResponse(userPropensity.getId(), propensityAnalysis);
    }

}
