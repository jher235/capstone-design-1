package org.example.capstonedesign1.domain.cardproduct.entity;

import jakarta.persistence.*;
import org.example.capstonedesign1.domain.propensity.entity.enums.Propensity;
import org.example.capstonedesign1.global.common.BaseEntity;

@Entity
public class CardProductPropensity extends BaseEntity {
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Propensity propensity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "card_product_id", nullable = false)
    private CardProduct cardProduct;
}
