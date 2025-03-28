package org.example.capstonedesign1.domain.bankproduct.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.capstonedesign1.domain.bankproduct.dto.json.BankProductRecommendationContent;
import org.example.capstonedesign1.domain.bankproduct.dto.request.BankProductRecommendRequest;
import org.example.capstonedesign1.domain.bankproduct.dto.response.BankProductRecommendationResponse;
import org.example.capstonedesign1.domain.bankproduct.entity.BankProduct;
import org.example.capstonedesign1.domain.bankproduct.entity.BankProductRecommendation;
import org.example.capstonedesign1.domain.bankproduct.repository.BankProductRecommendationRepository;
import org.example.capstonedesign1.domain.bankproduct.repository.BankProductRepository;
import org.example.capstonedesign1.domain.chat.client.OpenAiApiClient;
import org.example.capstonedesign1.domain.chat.dto.Message;
import org.example.capstonedesign1.domain.chat.dto.response.OpenAiResponse;
import org.example.capstonedesign1.domain.propensity.entity.enums.Propensity;
import org.example.capstonedesign1.domain.user.entity.User;
import org.example.capstonedesign1.domain.user.service.UserQueryService;
import org.example.capstonedesign1.global.template.PromptTemplate;
import org.example.capstonedesign1.global.util.JsonUtil;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.capstonedesign1.domain.chat.client.OpenAiApiClient.SYSTEM_ROLE;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankProductCommandService {
    private final UserQueryService userQueryService;

    private final BankProductRepository bankProductRepository;
    private final BankProductRecommendationRepository bankProductRecommendationRepository;

    private final OpenAiApiClient openAiApiClient;

    /**
     * 유저의 성향, 추천 요구사항에 맞는 상품 목록들을 DB에서 가져온 후 유저 정보와 금융 상품 목록을 바탕으로 금융 상품 추천 진행
     * @param user
     * @param request
     * @return
     */
    public BankProductRecommendationResponse recommendBankProduct(User user, BankProductRecommendRequest request){
        Propensity propensity = userQueryService.ensureUserPropensity(user);

        List<BankProduct> bankProducts =
                bankProductRepository.findRecommendable(propensity, request.getAmount(), request.getTerm());

        String requestMessage = PromptTemplate.BankProductRecommendPrompt(user, request, bankProducts);
        String content = openAiApiClient.sendRequest(List.of(new Message(SYSTEM_ROLE, requestMessage)));

        BankProductRecommendationContent bankProductRecommendationContent =
                JsonUtil.parseClass(BankProductRecommendationContent.class, content);

        BankProductRecommendation recommendation = new BankProductRecommendation(user, content);
        bankProductRecommendationRepository.save(recommendation);
        return new BankProductRecommendationResponse(recommendation.getId(),
                bankProductRecommendationContent,
                recommendation.getCreatedAt());
    }

}
