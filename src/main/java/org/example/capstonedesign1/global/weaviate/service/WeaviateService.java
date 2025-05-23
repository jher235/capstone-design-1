package org.example.capstonedesign1.global.weaviate.service;

import static org.example.capstonedesign1.global.constant.WeaviateConstant.*;

import java.util.List;
import java.util.stream.Collectors;

import org.example.capstonedesign1.domain.bankproduct.entity.BankProduct;
import org.example.capstonedesign1.domain.bankproduct.repository.BankProductRepository;
import org.example.capstonedesign1.domain.cardproduct.entity.CardProduct;
import org.example.capstonedesign1.domain.cardproduct.repository.CardProductRepository;
import org.example.capstonedesign1.global.exception.InternalServerException;
import org.example.capstonedesign1.global.exception.code.ErrorCode;
import org.example.capstonedesign1.global.openai.client.OpenAiClient;
import org.springframework.stereotype.Service;

import io.weaviate.client.WeaviateClient;
import io.weaviate.client.base.Result;
import io.weaviate.client.v1.batch.api.ObjectsBatcher;
import io.weaviate.client.v1.batch.model.ObjectGetResponse;
import io.weaviate.client.v1.data.model.WeaviateObject;
import io.weaviate.client.v1.schema.model.WeaviateClass;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class WeaviateService {

	private final WeaviateClient weaviateClient;
	private final OpenAiClient openAiClient;

	private final BankProductRepository bankProductRepository;
	private final CardProductRepository cardProductRepository;

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
