package org.example.capstonedesign1.domain.bankproduct.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.example.capstonedesign1.domain.propensity.entity.enums.Propensity;
import org.example.capstonedesign1.global.common.BaseEntity;

@Entity
@Getter
public class BankProductPropensity extends BaseEntity {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Propensity propensity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bank_product_id", nullable = false)
    private BankProduct bankProduct;

}
