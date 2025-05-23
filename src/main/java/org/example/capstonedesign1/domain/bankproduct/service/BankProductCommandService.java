package org.example.capstonedesign1.domain.bankproduct.service;

import static org.example.capstonedesign1.global.openai.client.OpenAiClient.*;

import java.util.List;

import org.example.capstonedesign1.domain.bankproduct.dto.json.BankProductRecommendationContent;
import org.example.capstonedesign1.domain.bankproduct.dto.request.BankProductRecommendRequest;
import org.example.capstonedesign1.domain.bankproduct.dto.response.BankProductRecommendationResponse;
import org.example.capstonedesign1.domain.bankproduct.entity.BankProduct;
import org.example.capstonedesign1.domain.bankproduct.entity.BankProductRecommendation;
import org.example.capstonedesign1.domain.bankproduct.repository.BankProductRecommendationRepository;
import org.example.capstonedesign1.domain.bankproduct.repository.BankProductRepository;
import org.example.capstonedesign1.domain.propensity.entity.enums.Propensity;
import org.example.capstonedesign1.domain.user.entity.User;
import org.example.capstonedesign1.domain.user.service.UserQueryService;
import org.example.capstonedesign1.global.dto.response.Message;
import org.example.capstonedesign1.global.openai.client.OpenAiClient;
import org.example.capstonedesign1.global.openai.template.PromptTemplate;
import org.example.capstonedesign1.global.util.JsonUtil;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankProductCommandService {
	private final UserQueryService userQueryService;

	private final BankProductRepository bankProductRepository;
	private final BankProductRecommendationRepository bankProductRecommendationRepository;

	private final OpenAiClient openAiClient;

	/**
	 * 유저의 성향, 추천 요구사항에 맞는 상품 목록들을 DB에서 가져온 후 유저 정보와 금융 상품 목록을 바탕으로 금융 상품 추천 진행
	 * strategy 는 정규화하지만, 추천 받은 상품 목록은 json으로 저장함. json으로 저장 시 칼럼명이 함께 저장되고 json 형태를 유지하므로 사이즈가 늘어난다는 단점이 존재하나
	 * 과도한 정규화로 인해 조회 시 join 연산을 불필요하게 수행하는 것을 방지하기 위해 json으로 저장함
	 *
	 * @param user
	 * @param request
	 * @return
	 */
	public BankProductRecommendationResponse recommendBankProduct(User user, BankProductRecommendRequest request) {
		//        Propensity propensity = userQueryService.ensureUserPropensity(user); // 현재는 요구사항 상 유저의 성향을 선택하지 않으므로 주석 처리 됨
		Propensity propensity = request.getPropensity();

		List<BankProduct> bankProducts =
			bankProductRepository.findRecommendable(propensity, request.getAmount(), request.getTerm());

		String requestMessage = PromptTemplate.bankProductRecommendPrompt(user, request, bankProducts);
		String content = openAiClient.sendRequest(List.of(new Message(SYSTEM_ROLE, requestMessage)));

		BankProductRecommendationContent bankProductRecommendationContent =
			JsonUtil.parseClass(BankProductRecommendationContent.class, content);

		String recommendationJson = JsonUtil.convertToJson(bankProductRecommendationContent.recommendations());

		BankProductRecommendation recommendation = new BankProductRecommendation(
			user, bankProductRecommendationContent.strategy(), recommendationJson);
		bankProductRecommendationRepository.save(recommendation);
		return BankProductRecommendationResponse.from(recommendation);
	}

}
