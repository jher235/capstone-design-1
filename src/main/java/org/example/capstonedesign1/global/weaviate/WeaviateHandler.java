package org.example.capstonedesign1.global.weaviate;

import static org.example.capstonedesign1.global.constant.WeaviateConstant.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.example.capstonedesign1.domain.bankproduct.entity.BankProduct;
import org.example.capstonedesign1.domain.bankproduct.repository.BankProductRepository;
import org.example.capstonedesign1.domain.cardproduct.entity.CardProduct;
import org.example.capstonedesign1.domain.cardproduct.repository.CardProductRepository;
import org.example.capstonedesign1.domain.conversation.dto.response.WeaviateVectorSimilarResponse;
import org.example.capstonedesign1.global.exception.InternalServerException;
import org.example.capstonedesign1.global.exception.code.ErrorCode;
import org.example.capstonedesign1.global.openai.client.OpenAiClient;
import org.example.capstonedesign1.global.util.JsonUtil;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import io.weaviate.client.WeaviateClient;
import io.weaviate.client.base.Result;
import io.weaviate.client.v1.batch.api.ObjectsBatcher;
import io.weaviate.client.v1.batch.model.ObjectGetResponse;
import io.weaviate.client.v1.data.model.WeaviateObject;
import io.weaviate.client.v1.graphql.model.GraphQLGetBaseObject;
import io.weaviate.client.v1.graphql.model.GraphQLResponse;
import io.weaviate.client.v1.graphql.query.argument.NearVectorArgument;
import io.weaviate.client.v1.graphql.query.fields.Field;
import io.weaviate.client.v1.schema.model.WeaviateClass;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@RequiredArgsConstructor
public class WeaviateHandler {

	private final WeaviateClient weaviateClient;
	private final OpenAiClient openAiClient;

	private final BankProductRepository bankProductRepository;
	private final CardProductRepository cardProductRepository;

	public List<UUID> getSimilarBankProduct(NearVectorArgument nearVector) {
		Result<GraphQLResponse> result = getGraphQL(nearVector,
			WEAVIATE_BANK_PRODUCT_COLLECTION,
			WEAVIATE_MAX_COUNT_SIMILAR_BANK_PRODUCT);

		WeaviateVectorSimilarResponse weaviateVectorSimilarResponse = convertFromGson(
			WeaviateVectorSimilarResponse.class, result);

		return weaviateVectorSimilarResponse.getGet().getBankProductInfos().stream()
			.map(WeaviateVectorSimilarResponse.ResultBankProductData::getAdditional)
			.map(GraphQLGetBaseObject.Additional::getId)
			.map(UUID::fromString)
			.toList();
	}

	public List<UUID> getSimilarCardProduct(NearVectorArgument nearVector) {
		Result<GraphQLResponse> result = getGraphQL(nearVector,
			WEAVIATE_CARD_PRODUCT_COLLECTION,
			WEAVIATE_MAX_COUNT_SIMILAR_CARD_PRODUCT);

		WeaviateVectorSimilarResponse weaviateVectorSimilarResponse = convertFromGson(
			WeaviateVectorSimilarResponse.class, result);

		return weaviateVectorSimilarResponse.getGet().getCardProductInfos().stream()
			.map(WeaviateVectorSimilarResponse.ResultCardProductData::getAdditional)
			.map(GraphQLGetBaseObject.Additional::getId)
			.map(UUID::fromString)
			.toList();
	}

	private Result<GraphQLResponse> getGraphQL(NearVectorArgument nearVector, String collection, int maxCount) {
		Result<GraphQLResponse> result = weaviateClient.graphQL().get()
			.withClassName(collection)
			.withNearVector(nearVector)
			.withFields(getAdditionalField())
			.withLimit(maxCount)
			.run();

		if (result.hasErrors()) {
			throw new InternalServerException(ErrorCode.WEAVIATE_GRAPHQL_FAILED,
				result.getError().getMessages().toString());
		}
		return result;
	}

	public void createBankProductSchema() {
		WeaviateClass bankProduct = WeaviateClass.builder()
			.className(WEAVIATE_BANK_PRODUCT_COLLECTION)
			.description("예적금 상품")
			// .properties(Collections.singletonList(
			// 	Property.builder()
			// 		.name("bankProductId")
			// 		.dataType(Collections.singletonList("uuid"))
			// 		.description("예적금 상품 id")
			// 		.build()))
			.build();

		Result<Boolean> result = weaviateClient.schema()
			.classCreator()
			.withClass(bankProduct)
			.run();

		if (result.hasErrors()) {
			log.error(result.getError().getMessages());
			throw new InternalServerException(ErrorCode.WEAVIATE_ERROR, "BankProduct 스키마 생성 중 오류 발생");
		}
	}

	public void createCardProductSchema() {
		WeaviateClass cardProduct = WeaviateClass.builder()
			.className(WEAVIATE_CARD_PRODUCT_COLLECTION)
			.description("카드 상품")
			// .properties(Collections.singletonList(
			// 	Property.builder()
			// 		.name("cardProductId")
			// 		.dataType(Collections.singletonList("uuid"))
			// 		.description("카드 상품 id")
			// 		.build()))
			.build();

		Result<Boolean> result = weaviateClient.schema()
			.classCreator()
			.withClass(cardProduct)
			.run();

		if (result.hasErrors()) {
			log.error(result.getError().getMessages());
			throw new InternalServerException(ErrorCode.WEAVIATE_ERROR, "CardProduct 스키마 생성 중 오류 발생");
		}
	}

	public void setBankProductDataFromDB() {
		List<BankProduct> bankProducts = bankProductRepository.findAll();
		bankProductBulkInsert(bankProducts);
	}

	public void setCardProductDataFromDB() {
		List<CardProduct> cardProducts = cardProductRepository.findAll();
		cardProductBulkInsert(cardProducts);
	}

	public void bankProductBulkInsert(List<BankProduct> bankProducts) {
		List<WeaviateObject> weaviateObjects = bankProducts.stream()
			.map(bankProduct -> {
				Float[] vector = openAiClient.sendEmbeddingRequest(bankProductEmbeddingString(bankProduct));

				return WeaviateObject.builder()
					.className(WEAVIATE_BANK_PRODUCT_COLLECTION)
					.id(bankProduct.getId().toString()) // 고유 ID를 RDB 내 ID와 통일
					.vector(vector)
					.build();
			}).collect(Collectors.toList());

		// 배치 삽입 및 실행
		ObjectsBatcher batcher = weaviateClient.batch().objectsBatcher();
		for (WeaviateObject weaviateObject : weaviateObjects) {
			batcher.withObject(weaviateObject);
		}
		Result<ObjectGetResponse[]> result = batcher.run();

		if (result.hasErrors()) {
			throw new InternalServerException(ErrorCode.WEAVIATE_ERROR);
		}
	}

	public void cardProductBulkInsert(List<CardProduct> cardProducts) {
		List<WeaviateObject> weaviateObjects = cardProducts.stream()
			.map(cardProduct -> {
				Float[] vector = openAiClient.sendEmbeddingRequest(cardProductEmbeddingString(cardProduct));

				return WeaviateObject.builder()
					.className(WEAVIATE_CARD_PRODUCT_COLLECTION)
					.id(cardProduct.getId().toString()) // 고유 ID를 RDB 내 ID와 통일
					.vector(vector)
					.build();
			}).collect(Collectors.toList());

		// 배치 삽입 및 실행
		ObjectsBatcher batcher = weaviateClient.batch().objectsBatcher();
		for (WeaviateObject weaviateObject : weaviateObjects) {
			batcher.withObject(weaviateObject);
		}
		Result<ObjectGetResponse[]> result = batcher.run();

		if (result.hasErrors()) {
			throw new InternalServerException(ErrorCode.WEAVIATE_ERROR);
		}
	}

	private <T> T convertFromGson(Class<T> dataType, Result<GraphQLResponse> result) {
		Gson gson = new Gson();
		String jsonResult = gson.toJson(result.getResult().getData());
		return JsonUtil.parseClass(dataType, jsonResult);
	}

	private Field getAdditionalField() {
		return Field.builder()
			.name("_additional")
			.fields(new Field[] {
				Field.builder().name("id").build(),
				Field.builder().name("certainty").build(),
				Field.builder().name("distance").build()
			})
			.build();
	}

	private String bankProductEmbeddingString(BankProduct bankProduct) {
		return bankProduct.getName()
			+ bankProduct.getBankProductType()
			+ bankProduct.getEligibility();
	}

	private String cardProductEmbeddingString(CardProduct cardProduct) {
		return cardProduct.getName()
			+ cardProduct.getBenefit();
	}

}
