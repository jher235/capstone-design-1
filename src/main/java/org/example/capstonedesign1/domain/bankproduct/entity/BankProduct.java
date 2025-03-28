package org.example.capstonedesign1.domain.bankproduct.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.capstonedesign1.domain.bankproduct.entity.enums.ProductType;
import org.example.capstonedesign1.global.common.BaseEntity;

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
    private ProductType productType;
    @Column(nullable = false)
    private String bankName;
    private String description;

    private Double interestRate6mPct;
    private Double interestRate12mPct;
    private Double interestRate24mPct;
    private Double interestRate36mPct;

    private Long minAmount;
    private Long maxAmount;
    private Integer minTerm; // 개월 수
    private Integer maxTerm;

    private String eligibility; // 가입 조건
    private Double earlyTerminationFee; // 중도 해지 수수료
    private String detailUrl;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("BankProduct{");
        sb.append("id=").append(this.getId())
                .append(", name=").append(name)
                .append(", productType=").append(productType)
                .append(", bankName=").append(bankName)
                .append(", description=").append(description != null ? description : "없음");

        // 이자율을 간결하게 표현
        sb.append(", interestRatePct (6/12/24/36m)=")
                .append(String.format("[%.2f, %.2f, %.2f, %.2f]",
                        interestRate6mPct != null ? interestRate6mPct : 0.0,
                        interestRate12mPct != null ? interestRate12mPct : 0.0,
                        interestRate24mPct != null ? interestRate24mPct : 0.0,
                        interestRate36mPct != null ? interestRate36mPct : 0.0));

        // 최소/최대 금액과 기간
        sb.append(", amount(won)=").append(minAmount).append(" ~ ").append(maxAmount != null ? maxAmount : "무제한")
                .append(", term(month)=").append(minTerm != null ? minTerm : "0").append(" ~ ").append(maxTerm != null ? maxTerm : "무제한");

        sb.append(", eligibility=").append(eligibility != null ? eligibility : "제한 없음")
                .append(", detailUrl=").append(detailUrl != null ? detailUrl : "없음")
                .append("}");

        return sb.toString();
    }

}
