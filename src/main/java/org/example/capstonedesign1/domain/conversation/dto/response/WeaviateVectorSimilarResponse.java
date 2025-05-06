package org.example.capstonedesign1.domain.conversation.dto.response;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

import static org.example.capstonedesign1.global.constant.WeaviateConstant.WEAVIATE_BANK_PRODUCT_COLLECTION;
import static org.example.capstonedesign1.global.constant.WeaviateConstant.WEAVIATE_CARD_PRODUCT_COLLECTION;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WeaviateVectorSimilarResponse {

    @SerializedName("Get")
    private GetData result;

    @Getter
    @NoArgsConstructor
    public static class ResultBankProductData {
        private UUID bankProductId;
    }

    @Getter
    @NoArgsConstructor
    public static class ResultCardProductData {
        private UUID cardProductId;
    }

    @Getter
    @NoArgsConstructor
    public static class GetData {

        @SerializedName(WEAVIATE_BANK_PRODUCT_COLLECTION)
        private List<ResultBankProductData> bankProductInfos;

        @SerializedName(WEAVIATE_CARD_PRODUCT_COLLECTION)
        private List<ResultCardProductData> cardProductInfos;

    }
}
