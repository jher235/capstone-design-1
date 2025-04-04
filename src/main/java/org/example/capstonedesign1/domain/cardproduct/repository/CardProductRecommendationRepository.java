package org.example.capstonedesign1.domain.cardproduct.repository;

import org.example.capstonedesign1.domain.cardproduct.entity.CardProductRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CardProductRecommendationRepository extends JpaRepository<CardProductRecommendation, UUID> {
}
