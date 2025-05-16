package org.example.capstonedesign1.domain.cardproduct.entity;

import java.util.ArrayList;
import java.util.List;

import org.example.capstonedesign1.domain.cardproduct.entity.enums.CardProductType;
import org.example.capstonedesign1.global.common.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;

@Entity
@Getter
public class CardProduct extends BaseEntity {

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "cardProduct")
	List<CardProductCategory> cardProductCategories = new ArrayList<>();
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
	private String imageUrl;

	@Override
	public String toString() {
		String sb = "CardProduct{" + "id=" + this.getId()
			+ ", name=" + name
			+ ", type=" + cardProductType
			+ ", bankName=" + bankName
			+ ", description=" + description
			+ ", annualFee=" + (this.annualFee != null ? annualFee : 0)
			+ ", benefit=" + benefit
			+ ", detailUrl=" + detailUrl
			+ ", imageUrl=" + imageUrl
			+ "}";
		return sb;
	}
}
