package org.example.capstonedesign1.global.openai.template;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.example.capstonedesign1.domain.bankproduct.dto.request.BankProductRecommendRequest;
import org.example.capstonedesign1.domain.bankproduct.entity.BankProduct;
import org.example.capstonedesign1.domain.cardproduct.entity.CardProduct;
import org.example.capstonedesign1.domain.propensity.dto.request.SurveyRequest;
import org.example.capstonedesign1.domain.user.entity.Profile;
import org.example.capstonedesign1.domain.user.entity.User;

public class PromptTemplate {
	private static final String BASE_TEMPLATE = """
		### 요청하고 싶은 것
		{{request}}
		        
		### 응답 값 형식
		{{responseFormat}}
		""";

	private static final String SURVEY_ENTRY_TEMPLATE = """
		### 질문: %s
		### 응답: %s
		            
		""";

	public static String conversationPrompt(String requestMessage,
		Optional<String> summary,
		List<BankProduct> bankProducts,
		List<CardProduct> cardProducts) {

		StringBuilder stringBuilder = new StringBuilder();
		bankProducts.stream()
			.forEach(product -> stringBuilder.append(product.toString()).append("\n\n"));
		cardProducts.stream()
			.forEach(product -> stringBuilder.append(product.toString()).append("\n\n"));

		return fillTemplate(
			"""
				## 명령
				금융 관련 대화를 진행하는 입장이야
				결과는 JSON 형식으로 반환해줘.
				                        
				## 사용자의 input
				%s
				                        
				## 이전 대화 요약
				%s
				                        
				## 관련 상품 목록
				%s
				                        
				                        
				""".formatted(requestMessage, summary.orElse("현재는 첫 대화로, 요약이 없음"), stringBuilder),
			"""
				{
				    "message": "<응답 메세지>",
				    "summary": "<이전 내용 + 현재 대화 내용을 모두 합친 요약>"           
				}
				"""
		);
	}

	public static String fillTemplate(String request, String responseFormat) {
		return BASE_TEMPLATE.replace("{{request}}", request)
			.replace("{{responseFormat}}", responseFormat);
	}

	//이걸 스트링 빌더로 묶어서 처리
	public static String entry(String question, String answer) {
		return String.format(SURVEY_ENTRY_TEMPLATE, question, answer);
	}

	public static String propensityAnalysisPrompt(String gender, String analyzedPoint) {
		return fillTemplate(
			"""
				## 명령
				성별과 사용자의 설문 결과로 추출한 성향 점수를 기반으로 금융 성향 타입
				(보수형(저축과 안정 추구), 소비형(즉각적 소비 선호), 투자형(위험 감수 및 수익 추구), 균형형(저축과 소비의 조화)**, 융합형(상황에 따라 유동적으로 반응)**) 을 분석하고, 
				타입에 대한 설명, 장단점, 주의점을 기계적이지 않고 자세하게 답변해줘.
				결과는 JSON 형식으로 반환해줘.

				## 사용자 정보
				성별: %s

				## 사용자의 성향 점수
				%s
				""".formatted(gender, analyzedPoint),
			"""
				{
				    "type": "<금융 성향 유형>",
				    "description": "<금융 성향 설명>",
				    "prosAndCons": "<금융 성향 장단점 설명>",
				    "precaution": "<해당 금융 성향인들 주의점>"
				}
				"""
		);
	}

	public static String propensityAnalysisPrompt(String gender, List<SurveyRequest.SurveyItemEntry> surveyEntries) {
		StringBuilder stringBuilder = new StringBuilder();
		surveyEntries.stream()
			.forEach(e -> stringBuilder.append(entry(e.getQuestion(), e.getSelectedAnswer())));

		return fillTemplate(
			"""
				## 명령
				성별과 사용자의 답변을 기반으로 금융 성향 타입
				(보수형(저축과 안정 추구), 소비형(즉각적 소비 선호), 투자형(위험 감수 및 수익 추구), 균형형(저축과 소비의 조화)**, 융합형(상황에 따라 유동적으로 반응)**) 을 분석하고, 
				타입에 대한 설명, 장단점, 주의점을 기계적이지 않고 자세하게 답변해줘.
				결과는 JSON 형식으로 반환해줘.

				## 사용자 정보
				성별: %s

				## 답변
				%s
				""".formatted(gender, stringBuilder),
			"""
				{
				    "type": "<금융 성향 유형>",
				    "description": "<금융 성향 설명>",
				    "prosAndCons": "<금융 성향 장단점 설명>",
				    "precaution": "<해당 금융 성향인들 주의점>"
				}
				"""
		);
	}

	public static String bankProductRecommendPrompt(User user, BankProductRecommendRequest request,
		List<BankProduct> products) {
		Profile profile = user.getProfile();
		StringBuilder stringBuilder = new StringBuilder();
		products.stream()
			.forEach(product -> stringBuilder.append(product.toString()).append("\n\n"));
		return fillTemplate(
			"""
				## 명령
				사용자 정보와 사용자의 요구사항을 기반으로 금융 상품을 추천해줘.
				금융 상품 목록은 DB 내의 사용자 금융 성향과 일치하는 상품들을 분류해놓은 거야.
				결과는 JSON 형식으로 반환해줘. 결과에서 recommendations 목록은 1 ~ 3개까지 넣어줘

				## 사용자 정보
				금융 성향: %s
				성별: %s
				만 나이: %s
				월 수입: %s
				자산: %s
				                
				## 사용자 요구
				투자 금액: %s 원
				투자 기간: %s 개월
				                
				## 금융 상품 목록
				%s

				""".formatted(
				profile.getPropensity().getName(),
				profile.getGender(),
				Period.between(profile.getBirthDate(), LocalDate.now()),
				profile.getSalary(),
				profile.getAsset(),
				request.getAmount(),
				request.getTerm(),
				stringBuilder
			),
			"""
				{
				  "recommendations": [
				    {
				      "id": "<금융 상품 ID>",
				      "description": "<금융 상품 설명>",
				      "reason": "<추천 이유 및 장단점>",
				      "detailUrl": "<상세 URL>"
				    }
				  ],
				  "strategy": "<금융 상품 추천 이유 및 전략 설명>"
				}
				"""
		);
	}

	public static String CardProductRecommendPrompt(User user, String paymentRecord, List<CardProduct> products) {
		Profile profile = user.getProfile();
		StringBuilder stringBuilder = new StringBuilder();
		products.stream()
			.forEach(product -> stringBuilder.append(product.toString()).append("\n\n"));
		return fillTemplate(
			"""
				## 명령
				사용자의 최근 결제 내역을 중심으로, 금융 성향 + 정보를 고려하여 카드 상품을 추천해줘.
				카드 상품 목록은 DB 내의 사용자 금융 성향과 일치하는 상품들을 분류해놓은 거야.
				결과는 JSON 형식으로 반환해줘. 결과에서 recommendations 목록은 1 ~ 3개까지 넣어줘

				## 사용자 정보
				금융 성향: %s
				성별: %s
				만 나이: %s
				월 수입: %s
				자산: %s
				                
				## 사용자의 최근 결제 내역
				%s
				                
				## 카드 상품 목록
				%s

				""".formatted(
				profile.getPropensity().getName(),
				profile.getGender(),
				Period.between(profile.getBirthDate(), LocalDate.now()),
				profile.getSalary(),
				profile.getAsset(),
				paymentRecord,
				stringBuilder
			),
			"""
				{
				  "recommendations": [
				    {
				      "id": "<카드 상품 ID>",
				      "description": "<카드 상품 설명>",
				      "reason": "<추천 이유 및 장단점>",
				      "detailUrl": "<상세 URL>"
				    }
				  ],
				  "strategy": "<종합적인 상품 추천 이유 및 전략 설명>"
				}
				"""
		);
	}

}
