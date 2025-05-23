package org.example.capstonedesign1.global.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	//BadRequest
	SIZE("4001", "길이가 유효하지 않습니다."),
	PATTERN("4001", "형식에 맞지 않습니다."),
	NOT_BLANK("4002", "필수값이 공백입니다."),
	LENGTH("4003", "길이가 유효하지 않습니다."),
	EMAIL("4004", "이메일 형식이 유효하지 않습니다."),
	NOT_NULL("4005", "필수값이 공백입니다."),
	INVALID_JSON_REQUEST("4006", "JSON 파싱 시 오류가 발생했습니다."),
	PROPENSITY_NOT_SET("4007", "아직 금융 성향 분석을 진행하지 않은 유저입니다."),
	INVALID_FILE("4008", "잘못된 파일 형식입니다."),

	USER_UNAUTHORIZED("4010", "로그인에 실패했습니다."),
	EXPIRED_JWT_TOKEN("4011", "만료된 JWT 토큰입니다."),
	INVALID_JWT_TOKEN("4012", "유효하지 않은 JWT 토큰입니다."),

	UN_AUTHORIZED("4030", "접근 권한이 없습니다."),
	PAYMENT_XLSX_UNAUTHORIZED("4031", "결제 내역 파일에 접근할 수 없습니다."),

	USER_NOT_FOUND("4040", "유저를 찾을 수 없습니다."),
	USER_PROPENSITY_NOT_FOUND("4041", "금융 성향 분석을 찾을 수 없습니다."),
	BANK_PRODUCT_RECOMMENDATION_NOT_FOUND("4042", "금융 상품 추천 결과를 찾을 수 없습니다."),
	CARD_PRODUCT_RECOMMENDATION_NOT_FOUND("4043", "카드 상품 추천 결과를 찾을 수 없습니다."),
	CONVERSATION_NOT_FOUND("4044", "대화를 찾을 수 없습니다."),

	CONFLICT_EMAIL("4090", "중복된 이메일입니다."),
	ALREADY_REGISTER_COMPLETED("4091", "이미 회원가입 완료한 유저입니다."),

	JSON_PARSING_FAILED("5000", "JSON 파싱 도중 오류가 발생했습니다."),
	WEAVIATE_GRAPHQL_FAILED("5001", " Weaviate 검색 도중 오류가 발생했습니다."),
	THREAD_INTERRUPT("5002", "스레드 인터럽트 오류가 발생했습니다."),
	ASYNC_ERROR("5003", "비동기 작업 중 오류가 발생했습니다."),
	WEAVIATE_ERROR("5004", "Weaviate 작업 도중 오류가 발생했습니다.");

	private final String status;
	private final String message;

	public static ErrorCode resolveValidationErrorCode(String code) {
		return switch (code) {
			case "Size" -> SIZE;
			case "Pattern" -> PATTERN;
			case "NotBlank" -> NOT_BLANK;
			case "Length" -> LENGTH;
			case "Email" -> EMAIL;
			case "NotNull" -> NOT_NULL;
			default -> throw new IllegalArgumentException("Unexpected value: " + code);
		};
	}
}
