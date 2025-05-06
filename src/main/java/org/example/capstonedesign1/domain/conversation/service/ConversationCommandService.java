package org.example.capstonedesign1.domain.conversation.service;

import com.google.gson.Gson;
import io.weaviate.client.WeaviateClient;
import io.weaviate.client.base.Result;
import io.weaviate.client.v1.graphql.model.GraphQLResponse;
import io.weaviate.client.v1.graphql.query.argument.NearVectorArgument;
import io.weaviate.client.v1.graphql.query.fields.Field;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.capstonedesign1.domain.bankproduct.entity.BankProduct;
import org.example.capstonedesign1.domain.bankproduct.service.BankProductQueryService;
import org.example.capstonedesign1.domain.cardproduct.entity.CardProduct;
import org.example.capstonedesign1.domain.cardproduct.service.CardProductQueryService;
import org.example.capstonedesign1.domain.conversation.dto.json.ConversationResponseContent;
import org.example.capstonedesign1.domain.conversation.dto.request.ConversationRequest;
import org.example.capstonedesign1.domain.conversation.dto.response.ConversationResponse;
import org.example.capstonedesign1.domain.conversation.dto.response.WeaviateVectorSimilarResponse;
import org.example.capstonedesign1.domain.conversation.entity.Conversation;
import org.example.capstonedesign1.domain.conversation.repository.ConversationRepository;
import org.example.capstonedesign1.domain.user.entity.User;
import org.example.capstonedesign1.global.dto.response.Message;
import org.example.capstonedesign1.global.exception.InternalServerException;
import org.example.capstonedesign1.global.exception.code.ErrorCode;
import org.example.capstonedesign1.global.openai.client.OpenAiApiClient;
import org.example.capstonedesign1.global.openai.template.PromptTemplate;
import org.example.capstonedesign1.global.util.JsonUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.example.capstonedesign1.global.constant.WeaviateConstant.*;


@Service
@RequiredArgsConstructor
@Log4j2
public class ConversationCommandService {

    private final BankProductQueryService bankProductQueryService;
    private final CardProductQueryService cardProductQueryService;

    private final ConversationRepository conversationRepository;
    private final OpenAiApiClient openAiApiClient;

    private final WeaviateClient weaviateClient;

    public ConversationResponse conversation(User user, boolean embedding, ConversationRequest request) {
        String requestMessage = request.requestMessage();
        Float[] queryVector = openAiApiClient.sendEmbeddingRequest(requestMessage);
        NearVectorArgument nearVector = getNearVector(queryVector);

        List<BankProduct> bankProducts = getSimilarBankProducts(nearVector);
        List<CardProduct> cardProducts = getSimilarCardProducts(nearVector);

        String prompt = PromptTemplate.conversationPrompt(requestMessage,
                Optional.ofNullable(request.summary()),
                bankProducts, cardProducts);
        log.info(prompt);

        String response = openAiApiClient.sendRequest(List.of(new Message(OpenAiApiClient.SYSTEM_ROLE, prompt)));
        ConversationResponseContent content = JsonUtil.parseClass(ConversationResponseContent.class, response);

        Conversation conversation = new Conversation(user, requestMessage, content.message(), content.summary());
        conversationRepository.save(conversation);
        return ConversationResponse.from(conversation);
    }


    private List<BankProduct> getSimilarBankProducts(NearVectorArgument nearVector) {
        Field idField = getField(WEAVIATE_BANK_PRODUCT_ID);
        Field additionalField = getAdditionalField();

        Result<GraphQLResponse> result = weaviateClient.graphQL().get()
                .withClassName(WEAVIATE_BANK_PRODUCT_COLLECTION)
                .withNearVector(nearVector)
                .withFields(idField, additionalField)
                .withLimit(WEAVIATE_MAX_COUNT_SIMILAR_BANK_PRODUCT)
                .run();

        if (result.hasErrors()) {
            throw new InternalServerException(ErrorCode.WEAVIATE_GRAPHQL_FAILED, result.getError().getMessages().toString());
        }

        WeaviateVectorSimilarResponse weaviateVectorSimilarResponse = convertFromGson(WeaviateVectorSimilarResponse.class, result);

        List<UUID> bankProductIds = weaviateVectorSimilarResponse.getResult().getBankProductInfos().stream()
                .map(WeaviateVectorSimilarResponse.ResultBankProductData::getBankProductId)
                .toList();

        return bankProductQueryService.findByIds(bankProductIds);
    }


    private List<CardProduct> getSimilarCardProducts(NearVectorArgument nearVector) {
        Field idField = getField(WEAVIATE_CARD_PRODUCT_ID);
        Field additionalField = getAdditionalField();

        Result<GraphQLResponse> result = weaviateClient.graphQL().get()
                .withClassName(WEAVIATE_CARD_PRODUCT_COLLECTION)
                .withNearVector(nearVector)
                .withFields(idField, additionalField)
                .withLimit(WEAVIATE_MAX_COUNT_SIMILAR_CARD_PRODUCT)
                .run();

        if (result.hasErrors()) {
            throw new InternalServerException(ErrorCode.WEAVIATE_GRAPHQL_FAILED, result.getError().getMessages().toString());
        }

        WeaviateVectorSimilarResponse weaviateVectorSimilarResponse = convertFromGson(WeaviateVectorSimilarResponse.class, result);

        List<UUID> cardProductIds = weaviateVectorSimilarResponse.getResult().getCardProductInfos().stream()
                .map(WeaviateVectorSimilarResponse.ResultCardProductData::getCardProductId)
                .toList();

        return cardProductQueryService.findByIds(cardProductIds);
    }

    private Field getField(String fieldName) {
        return Field.builder()
                .name(fieldName)
                .build();
    }

    private Field getAdditionalField() {
        return Field.builder()
                .name("_additional")
                .fields(new Field[]{
                        Field.builder().name("certainty").build(),
                        Field.builder().name("distance").build()
                })
                .build();
    }

    private NearVectorArgument getNearVector(Float[] queryVector) {
        return NearVectorArgument.builder()
                .vector(queryVector)
                .certainty(0.7f) // 유사도 임계값
                .build();
    }

    private <T> T convertFromGson(Class<T> dataType, Result<GraphQLResponse> result) {
        Gson gson = new Gson();
        String jsonResult = gson.toJson(result.getResult().getData());
        return gson.fromJson(jsonResult, dataType);
    }

}
