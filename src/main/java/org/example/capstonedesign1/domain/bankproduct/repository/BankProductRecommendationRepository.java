package org.example.capstonedesign1.domain.bankproduct.repository;

import org.example.capstonedesign1.domain.bankproduct.entity.BankProductRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BankProductRecommendationRepository extends JpaRepository<BankProductRecommendation, UUID> {
}
