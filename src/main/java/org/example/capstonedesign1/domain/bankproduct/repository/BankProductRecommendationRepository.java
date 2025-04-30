package org.example.capstonedesign1.domain.bankproduct.repository;

import org.example.capstonedesign1.domain.bankproduct.dto.response.projection.BankProductRecommendationPreview;
import org.example.capstonedesign1.domain.bankproduct.entity.BankProductRecommendation;
import org.example.capstonedesign1.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BankProductRecommendationRepository extends JpaRepository<BankProductRecommendation, UUID> {

    @Query("select b.id as id, b.strategy as strategy, b.createdAt as createdAt " +
            "from BankProductRecommendation as b " +
            "where b.user = :user")
    Page<BankProductRecommendationPreview> findBankProductRecommendationsByUser(@Param(value = "user") User user,
                                                                                Pageable pageable);

}
