package org.example.capstonedesign1.domain.conversation.dto.response;

import static org.example.capstonedesign1.global.constant.WeaviateConstant.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import io.weaviate.client.v1.graphql.model.GraphQLGetBaseObject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WeaviateVectorSimilarResponse {

	@SerializedName("Get")
	@JsonProperty("Get")
	private GetData get;

	@Getter
	@NoArgsConstructor
	public static class ResultBankProductData {
		@JsonProperty("_additional")
		private GraphQLGetBaseObject.Additional additional;
	}

	@Getter
	@NoArgsConstructor
	public static class ResultCardProductData {
		@JsonProperty("_additional")
		private GraphQLGetBaseObject.Additional additional;
	}

	@Getter
	@NoArgsConstructor
	public static class GetData {

		@SerializedName(WEAVIATE_BANK_PRODUCT_COLLECTION)
		@JsonProperty(WEAVIATE_BANK_PRODUCT_COLLECTION)
		private List<ResultBankProductData> bankProductInfos;

		@SerializedName(WEAVIATE_CARD_PRODUCT_COLLECTION)
		@JsonProperty(WEAVIATE_CARD_PRODUCT_COLLECTION)
		private List<ResultCardProductData> cardProductInfos;
	}

}
