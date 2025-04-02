package org.example.capstonedesign1.domain.cardproduct.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import org.example.capstonedesign1.domain.cardproduct.entity.enums.CardProductType;
import org.example.capstonedesign1.global.common.BaseEntity;

@Entity
@Getter
public class CardProduct extends BaseEntity {

    @Column(nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    private CardProductType cardProductType;
    @Column(nullable = false)
    private String bankName;

    private String description;
    private Integer annualFee;
    @Column(length = 1000)
    private String benefit;
    private String detailUrl;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CardProduct{");
        sb.append("id=" + this.getId())
                .append(", name=" + name)
                .append(", type=" + cardProductType)
                .append(", bankName=" + bankName);

        sb.append(this.description != null ? ", description=" + description : "")
                .append(", annualFee=").append(this.annualFee != null ? annualFee : 0)
                .append(this.benefit != null ?  ", benefit=" + benefit : "")
                .append(this.detailUrl != null ?  ", detailUrl=" + detailUrl : "");

        return sb.toString();
    }
}
