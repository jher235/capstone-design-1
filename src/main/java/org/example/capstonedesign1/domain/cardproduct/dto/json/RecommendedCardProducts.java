package org.example.capstonedesign1.domain.cardproduct.dto.json;

import java.util.List;

import org.example.capstonedesign1.global.util.JsonUtil;

import com.fasterxml.jackson.core.type.TypeReference;

public record RecommendedCardProducts(List<RecommendedCardProduct> recommendations) {

	public static RecommendedCardProducts fromString(String recommendations) {
		return new RecommendedCardProducts(JsonUtil.parseList(new TypeReference<List<RecommendedCardProduct>>() {
		}, recommendations));
	}
}
