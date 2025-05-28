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
		## 요청하고 싶은 것
		{{request}}
		        
		## 응답 값 형식 - 바로 parsing 할 수 있도록 아래 형식을 절대적으로 준수할 것.
		{{responseFormat}}
		""";

	private static final String SURVEY_ENTRY_TEMPLATE = """
		### 질문: %s
		### 응답: %s
		            
		""";

	public static String conversationPrompt(
		User user,
		String requestMessage,
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
				당신은 신뢰할 수 있는 금융 AI 어시스턴트입니다. 아래는 카드 상품 또는 금융 상품 설명과 사용자의 질문입니다.
				아래 문서를 바탕으로, 사용자의 금융 상황 또는 선호에 맞는 정확하고 구체적인 답변을 제공하세요.
				필요하다면 적절한 금융 용어를 사용하고, 쉽게 이해할 수 있게 설명하세요.
								
				## 사용자 정보
				%s
								         
				## 사용자의 input
				%s
				                        
				## 이전 대화 요약
				%s
				                        
				## RAG - 관련 상품 목록
				%s
				                        
				                        
				""".formatted(user.getInfoString(), requestMessage, summary.orElseGet(() -> "현재는 첫 대화로, 요약이 없음"),
				stringBuilder),
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
				당신은 금융 성향 분석 전문가입니다.
				    
				다음은 사용자의 성별과 여러 질문에 대한 응답입니다.
				이 데이터를 기반으로 사용자의 금융 성향을 분석해 주세요.
				    
				분석 결과는 다음 중 정확히 하나의 성향으로 분류해야 합니다:
				- 보수형
				- 소비형
				- 투자형
				- 균형형
				- 융합형
								
				다음의 항목들을 포함하여 결과를 JSON 형식으로 생성해 주세요:
				    
				1. type: 분석된 금융 성향 유형
				2. description: 해당 성향의 일반적인 특징
				3. prosAndCons: 해당 성향의 장점과 단점
				4. precaution: 이 성향을 가진 사람들이 주의해야 할 점
				    
				응답은 기계적이지 않고 자연스럽고 사람스러운 문장으로 구성해 주세요.
				    

				## 사용자 정보
				성별: %s

				## 답변
				%s
				""".formatted(gender, stringBuilder),
			"""
				{
				    "type": "<금융 성향 유형>",
				    "description": "<금융 성향 설명>",
				    "prosAndCons": "<장점과 단점>",
				    "precaution": "<주의할 점>"
				}
				"""
		);
	}

	public static String bankProductRecommendPrompt(User user,
		BankProductRecommendRequest request, List<BankProduct> products) {
		Profile profile = user.getProfile();
		StringBuilder stringBuilder = new StringBuilder();
		products.stream()
			.forEach(product -> stringBuilder.append(product.toString()).append("\n\n"));
		return fillTemplate(
			"""
				## 명령
				당신은 금융 상품을 추천해주는 AI입니다.
				    
				사용자의 금융 성향, 소득, 자산, 나이, 투자 금액 및 기간을 바탕으로 적절한 예금/적금 상품을 최대 3개까지 추천해 주세요.
				    
				각 추천 항목에 대해 다음 정보를 포함해 주세요:
				    
				- description:
				  - {상품명} - {상품에 대한 설명} 형태를 준수할 것
				  - 상품명, 유형(예금/적금), 판매 은행
				  - 적립 방식(자유/정기), 가입 조건(납입 금액 범위, 기간 범위)
				  - 해당 투자 기간의 이율 강조 (6/12/24/36개월 중 선택)
				  - 가입 대상 조건 요약
				    
				- reason:
				  - 사용자의 투자 조건과 어떻게 부합하는지
				  - 상품의 장점 2가지 이상 + 주의사항 1가지
				  - 가능하다면 수치를 포함해 실질적 혜택 예시 제공
				    
				- strategy:
				  - 사용자의 금융 성향과 상품의 특징이 어떤 점에서 잘 맞는지
				  - 투자 금액/기간과 상품의 조건 간의 연계 설명
				  - 사용자의 재무 안정성과 목표 달성 측면에서 어떤 이점이 있는지 설명
				    

				## 사용자 정보
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

	public static String consumeAnalysisPrompt(String categories, String consumptionRecord) {
		return fillTemplate(
			"""
				## 명령
				사용자의 최근 결제 내역을 분석하여 가장 소비 내역이 많은 카테고리를 3개 찾아주고
				소비 내역을 분석 및 요약해줘.
								
				1. 각 결제 내역을 아래 카테고리 목록 중 어디에 해당하는지 매핑
				2. 카테고리별 총 소비 금액을 계산
				3. 가장 소비가 많은 카테고리 Top3를 도출
				4. 전체 소비 패턴을 분석·요약
								
								
				## 카테고리 목록은 아래와 같아.
				%s
								
				## 사용자의 최근 결제 내역
				%s

				""".formatted(
				categories,
				consumptionRecord
			),
			"""
				{
				  "category": [
					 "<카테고리 목록에 존재하는 카테고리중 소비율이 가장 높은 Top3>"
				  ],
				  "analysis": "<소비 내역 분석 및 요약>"
				}
				"""
		);
	}

	public static String cardProductRecommendPrompt(User user, String paymentRecord, List<CardProduct> products) {
		Profile profile = user.getProfile();
		StringBuilder stringBuilder = new StringBuilder();
		products.stream()
			.forEach(product -> stringBuilder.append(product.toString()).append("\n\n"));
		return fillTemplate(
			"""
				## 명령
								
				당신은 신뢰할 수 있는 금융 어시스턴트입니다.
				    
				아래 사용자 정보를 참고하여 적절한 카드 상품을 최대 4개 추천해주세요.
				추천은 사용자의 소비 패턴(카테고리별 소비금액)과 카드 혜택의 매칭 정도를 중심으로 분석해 주세요.
				    
				각 추천 항목은 다음 형식을 따라주세요:
				- description: {상품명} - {주요 혜택 요약 (할인율, 조건, 전월 실적 등 포함)}
				- reason: 사용자의 소비 내역과 어떻게 매칭되는지 구체적 설명 (절약 가능 금액 예시 포함)
				- detailUrl: 카드 링크
				    
				또한, strategy 항목에 종합적인 추천 전략을 포함해주세요:
				- 사용자 프로필과 소비 패턴 기반 분석
				- 성향(예: 균형형)에 맞는 카드의 특성과 혜택 연결
				- 연회비, 접근성, 유용성 등도 고려
				     
				## 사용자 정보
				성별: %s
				만 나이: %s
				월 수입: %s
				자산: %s
				                
				## 사용자의 최근 결제 내역 요약
				%s
				                
				## 카드 상품 목록
				%s

				""".formatted(
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
				      "detailUrl": "<상세 URL>",
				      "imageUrl": "<카드 이미지 URL>"
				    }
				  ],
				  "strategy": "<종합적인 상품 추천 이유 및 전략 설명>"
				}
				"""
		);
	}

}
