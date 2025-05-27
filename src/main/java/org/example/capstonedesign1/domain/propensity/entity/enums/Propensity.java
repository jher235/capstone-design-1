package org.example.capstonedesign1.domain.propensity.entity.enums;

import java.util.Arrays;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Propensity {
	CONSERVATIVE("보수형", "저축과 안정 추구"),
	CONSUMER("소비형", "즉각적 소비 선호"),
	INVESTOR("투자형", "위험 감수 및 수익 추구"),
	BALANCED("균형형", "저축과 소비의 조화"),
	FLEXIBLE("융합형", "상황에 따라 유동적으로 반응");

	private String name;
	private String description;

	public static Propensity findByName(String typeName) {
		return switch (typeName) {
			case "보수형" -> CONSERVATIVE;
			case "소비형" -> CONSUMER;
			case "투자형" -> INVESTOR;
			case "균형형" -> BALANCED;
			case "융합형" -> FLEXIBLE;
			default -> throw new IllegalStateException("Unexpected value: " + typeName);
		};
	}

	public static String getAllPropensityNames() {
		return Arrays.stream(Propensity.values())
			.map(Propensity::getName)
			.collect(Collectors.joining(", "));
	}
}
