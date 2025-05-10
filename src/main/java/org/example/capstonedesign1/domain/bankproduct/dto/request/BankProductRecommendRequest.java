package org.example.capstonedesign1.domain.bankproduct.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.capstonedesign1.domain.propensity.entity.enums.Propensity;

import static org.example.capstonedesign1.domain.bankproduct.constant.BankProductConstant.MAX_INVEST_AMOUNT;
import static org.example.capstonedesign1.domain.bankproduct.constant.BankProductConstant.MIN_INVEST_AMOUNT;

@Getter
@NoArgsConstructor
public class BankProductRecommendRequest {

    @Schema(description = "투자 기간(개월)")
    @Positive(message = "투자 기간은 양수여야 합니다.")
    private Integer term;

    @Schema(
            description = "투자 금액(원)",
            minimum = "0",
            maximum = "10000000000000"
    )
    @NotNull(message = "투자 금액 값은 필수입니다.")
    @Min(value = 0, message = "투자 금액은 0원 이상이어야 합니다.")
    @Max(value = 10_000_000_000_000L, message = "투자 금액은 1경 원을 넘을 수 없습니다.")
    private Long amount;

    @Schema(description = """
            투자 성향
            CONSERVATIVE: 보수형,
            CONSUMER: 소비형,
            INVESTOR: 투자형,
            BALANCED: 균형형,
            FLEXIBLE: 융합형
            """,
            example = "CONSERVATIVE",
            allowableValues = {"CONSERVATIVE", "CONSUMER", "INVESTOR", "BALANCED", "FLEXIBLE"})
    @NotNull(message = "투자 성향은 필수입니다.")
    private Propensity propensity;

}
