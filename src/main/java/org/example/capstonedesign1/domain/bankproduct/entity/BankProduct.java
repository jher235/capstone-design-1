package org.example.capstonedesign1.domain.bankproduct.entity;

import java.math.BigDecimal;

import org.example.capstonedesign1.domain.propensity.entity.enums.Propensity;
import org.example.capstonedesign1.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BankProduct extends BaseEntity {

	@Enumerated(EnumType.STRING)
	private Propensity propensity;

	@Column(nullable = false)
	private String name;

	private String bankProductType;
	@Column(nullable = false)
	private String bankName;
	private String description;
	private String period;

	@Column(precision = 5, scale = 3)
	private BigDecimal interestRate6mPct;
	@Column(precision = 5, scale = 3)
	private BigDecimal interestRate12mPct;
	@Column(precision = 5, scale = 3)
	private BigDecimal interestRate24mPct;
	@Column(precision = 5, scale = 3)
	private BigDecimal interestRate36mPct;

	private String defaultInterestRate; // 기본 이율
	private String preferentialInterestRate; // 우대 이율

	private Long minAmount;
	private Long maxAmount;
	private Integer minTerm; // 개월 수
	private Integer maxTerm;

	private String eligibility; // 가입 조건
	private String earlyTermination; // 중도 해지 조건
	private String detailUrl;

	@Override
	public String toString() {

		String sb = "BankProduct{" + "id=" + this.getId() +
			", name=" + name +
			", productType=" + bankProductType +
			", bankName=" + bankName +
			", period=" + period +
			", description=" + (description != null ? description : "없음") +

			// 이자율을 간결하게 표현
			", interestRatePct (6/12/24/36m)=" +
			String.format("[%.2f, %.2f, %.2f, %.2f]",
				interestRate6mPct != null ? interestRate6mPct : 0.0,
				interestRate12mPct != null ? interestRate12mPct : 0.0,
				interestRate24mPct != null ? interestRate24mPct : 0.0,
				interestRate36mPct != null ? interestRate36mPct : 0.0) +
			", preferentialInterestRate=" + preferentialInterestRate +

			// 최소/최대 금액과 기간
			", amount(won)=" + minAmount + " ~ " + (maxAmount != null ? maxAmount : "무제한") +
			", term(month)=" + (minTerm != null ? minTerm : "0") + " ~ " + (maxTerm != null ? maxTerm : "무제한") +
			", earlyTermination=" + (eligibility != null ? eligibility : "제한 없음") +
			", detailUrl=" + (detailUrl != null ? detailUrl : "없음") +
			"}";

		return sb;
	}

}
