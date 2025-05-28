package org.example.capstonedesign1.domain.cardproduct.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.example.capstonedesign1.domain.cardproduct.dto.json.CardProductRecommendationContent;
import org.example.capstonedesign1.domain.cardproduct.dto.json.ConsumptionAnalysis;
import org.example.capstonedesign1.domain.cardproduct.dto.response.CardProductRecommendationResponse;
import org.example.capstonedesign1.domain.cardproduct.entity.CardProduct;
import org.example.capstonedesign1.domain.cardproduct.entity.CardProductRecommendation;
import org.example.capstonedesign1.domain.cardproduct.entity.Category;
import org.example.capstonedesign1.domain.cardproduct.repository.CardProductRecommendationRepository;
import org.example.capstonedesign1.domain.cardproduct.repository.CardProductRepository;
import org.example.capstonedesign1.domain.user.entity.User;
import org.example.capstonedesign1.global.dto.response.Message;
import org.example.capstonedesign1.global.openai.client.OpenAiClient;
import org.example.capstonedesign1.global.openai.template.PromptTemplate;
import org.example.capstonedesign1.global.util.JsonUtil;
import org.example.capstonedesign1.global.xlsx.XlsxHandler;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class CardProductCommandService {
	private static final String DELIMITER = ", ";
	private static final int MAX_RECOMMENDABLE_CARD_PRODUCT_COUNT = 10;

	private final CategoryQueryService categoryQueryService;

	private final CardProductRepository cardProductRepository;
	private final CardProductRecommendationRepository cardProductRecommendationRepository;

	private final XlsxHandler xlsxHandler;
	private final OpenAiClient openAiClient;

	public CardProductRecommendationResponse recommendCardProduct(
		User user,
		MultipartFile file,
		Optional<String> filePassword
	) {
		List<Category> categories = categoryQueryService.getAllCategory(); // 캐싱 적용하기.

		ConsumptionAnalysis consumptionAnalysis = getConsumeAnalysis(user, file, filePassword);

		List<Category> categoryList = getConsumedCategory(categories, consumptionAnalysis.category());
		List<CardProduct> recommendableCardProducts = cardProductRepository.findByCategoryList(categoryList,
			PageRequest.ofSize(MAX_RECOMMENDABLE_CARD_PRODUCT_COUNT));

		CardProductRecommendationContent content =
			recommendFromProductsAndAnalysis(user, consumptionAnalysis.analysis(), recommendableCardProducts);
		String recommendationJson = JsonUtil.convertToJson(content.recommendations());

		CardProductRecommendation savedRecommendation = cardProductRecommendationRepository.save(
			CardProductRecommendation.builder()
				.user(user)
				.consumptionAnalysis(consumptionAnalysis.analysis())
				.strategy(content.strategy())
				.content(recommendationJson)
				.build());
		return CardProductRecommendationResponse.of(savedRecommendation, content.recommendations());
	}

	private CardProductRecommendationContent recommendFromProductsAndAnalysis(
		User user,
		String consumptionAnalysis,
		List<CardProduct> cardProducts) {
		List<CardProduct> recommendableCardProducts =
			cardProducts.subList(0, Math.min(cardProducts.size(), MAX_RECOMMENDABLE_CARD_PRODUCT_COUNT));

		String prompt = PromptTemplate.cardProductRecommendPrompt(user, consumptionAnalysis, recommendableCardProducts);
		String response = openAiClient.sendRequest(List.of(new Message(OpenAiClient.SYSTEM_ROLE, prompt)));

		return JsonUtil.parseClass(CardProductRecommendationContent.class, response);
	}

	private ConsumptionAnalysis getConsumeAnalysis(
		User user,
		MultipartFile file,
		Optional<String> filePassword
	) {
		xlsxHandler.validXlsxFile(file);
		String consumptionRecord = xlsxHandler.resolveConsumptionRecord(user, file, filePassword);
		String prompt = PromptTemplate.consumeAnalysisPrompt(getAllCategoryNameToString(), consumptionRecord);
		String response = openAiClient.sendRequest(List.of(new Message(OpenAiClient.SYSTEM_ROLE, prompt)));

		return JsonUtil.parseClass(ConsumptionAnalysis.class, response);
	}

	private List<Category> getConsumedCategory(List<Category> allCategories, List<String> categoryNames) {
		return allCategories.stream()
			.filter(category -> categoryNames.contains(category.getName()))
			.toList();
	}

	private String getAllCategoryNameToString() {
		List<Category> categories = categoryQueryService.getAllCategory();
		return categories.stream()
			.map(Category::getName)
			.collect(Collectors.joining(DELIMITER));
	}

}
