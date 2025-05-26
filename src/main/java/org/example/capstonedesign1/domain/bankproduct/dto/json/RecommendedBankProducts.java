package org.example.capstonedesign1.domain.bankproduct.dto.json;

import java.util.List;

import org.example.capstonedesign1.global.util.JsonUtil;

import com.fasterxml.jackson.core.type.TypeReference;

public record RecommendedBankProducts(List<RecommendedBankProduct> recommendations) {
	public static RecommendedBankProducts fromString(String recommendations) {
		return new RecommendedBankProducts(JsonUtil.parseList(new TypeReference<List<RecommendedBankProduct>>() {
		}, recommendations));
	}
}
