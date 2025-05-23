package org.example.capstonedesign1.domain.propensity.service;

import static org.example.capstonedesign1.global.openai.client.OpenAiClient.*;

import java.util.List;

import org.example.capstonedesign1.domain.propensity.dto.json.PropensityAnalysis;
import org.example.capstonedesign1.domain.propensity.dto.request.PropensityAnalysisRequest;
import org.example.capstonedesign1.domain.propensity.dto.response.PropensityAnalysisResponse;
import org.example.capstonedesign1.domain.propensity.entity.UserPropensity;
import org.example.capstonedesign1.domain.propensity.entity.enums.Propensity;
import org.example.capstonedesign1.domain.propensity.repository.UserPropensityRepository;
import org.example.capstonedesign1.domain.user.entity.User;
import org.example.capstonedesign1.global.dto.response.Message;
import org.example.capstonedesign1.global.openai.client.OpenAiClient;
import org.example.capstonedesign1.global.openai.template.PromptTemplate;
import org.example.capstonedesign1.global.util.JsonUtil;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PropensityCommandService {

	private final OpenAiClient openAiClient;

	private final UserPropensityRepository userPropensityRepository;

	public PropensityAnalysisResponse submitSurvey(User user, PropensityAnalysisRequest request) {
		String requestMessage = PromptTemplate.propensityAnalysisPrompt(
			user.getProfile().getGender().toString(),
			request.userSurveyResult());

		String content = openAiClient.sendRequest(List.of(new Message(SYSTEM_ROLE, requestMessage)));

		PropensityAnalysis propensityAnalysis = JsonUtil.parseClass(PropensityAnalysis.class, content);
		Propensity propensity = Propensity.findByName(propensityAnalysis.type());
		UserPropensity userPropensity = new UserPropensity(user, propensity, content);
		userPropensityRepository.save(userPropensity);
		return new PropensityAnalysisResponse(userPropensity.getId(), propensityAnalysis,
			userPropensity.getCreatedAt());
	}

}
