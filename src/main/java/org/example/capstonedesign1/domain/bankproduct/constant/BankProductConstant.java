package org.example.capstonedesign1.domain.bankproduct.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BankProductConstant {

    public static final Long MIN_INVEST_AMOUNT = 0L;
    public static final Long MAX_INVEST_AMOUNT = 10_000_000_000_000L;
}
