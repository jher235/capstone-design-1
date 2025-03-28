package org.example.capstonedesign1.domain.bankproduct.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

}
