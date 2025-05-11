package org.example.capstonedesign1.domain.bankproduct.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.capstonedesign1.domain.bankproduct.entity.enums.BankProductType;
import org.example.capstonedesign1.global.common.BaseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BankProduct extends BaseEntity {

    @OneToMany(mappedBy = "bankProduct", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BankProductPropensity> bankProductPropensities = new ArrayList<>();

    @Column(nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    private BankProductType bankProductType;
    @Column(nullable = false)
    private String bankName;
    private String description;

    @Column(precision = 5, scale = 3)
    private BigDecimal interestRate6mPct;
    @Column(precision = 5, scale = 3)
    private BigDecimal interestRate12mPct;
    @Column(precision = 5, scale = 3)
    private BigDecimal interestRate24mPct;
    @Column(precision = 5, scale = 3)
    private BigDecimal interestRate36mPct;

    private Long minAmount;
    private Long maxAmount;
    private Integer minTerm; // 개월 수
    private Integer maxTerm;

    private String eligibility; // 가입 조건
    private Double earlyTerminationFee; // 중도 해지 수수료
    private String detailUrl;

    @Override
    public String toString() {

        String sb = "BankProduct{" + "id=" + this.getId() +
                ", name=" + name +
                ", productType=" + bankProductType +
                ", bankName=" + bankName +
                ", description=" + (description != null ? description : "없음") +

                // 이자율을 간결하게 표현
                ", interestRatePct (6/12/24/36m)=" +
                String.format("[%.2f, %.2f, %.2f, %.2f]",
                        interestRate6mPct != null ? interestRate6mPct : 0.0,
                        interestRate12mPct != null ? interestRate12mPct : 0.0,
                        interestRate24mPct != null ? interestRate24mPct : 0.0,
                        interestRate36mPct != null ? interestRate36mPct : 0.0) +

                // 최소/최대 금액과 기간
                ", amount(won)=" + minAmount + " ~ " + (maxAmount != null ? maxAmount : "무제한") +
                ", term(month)=" + (minTerm != null ? minTerm : "0") + " ~ " + (maxTerm != null ? maxTerm : "무제한") +
                ", eligibility=" + (eligibility != null ? eligibility : "제한 없음") +
                ", detailUrl=" + (detailUrl != null ? detailUrl : "없음") +
                "}";

        return sb;
    }

}
