package org.example.capstonedesign1.global.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WeaviateConstant {
    public static final String WEAVIATE_BANK_PRODUCT_COLLECTION = "BankProduct";
    public static final String WEAVIATE_BANK_PRODUCT_ID = "bankProductId";
    public static final int WEAVIATE_MAX_COUNT_SIMILAR_BANK_PRODUCT = 4;

    public static final String WEAVIATE_CARD_PRODUCT_COLLECTION = "CardProduct";
    public static final String WEAVIATE_CARD_PRODUCT_ID = "cardProductId";
    public static final int WEAVIATE_MAX_COUNT_SIMILAR_CARD_PRODUCT = 4;
}
