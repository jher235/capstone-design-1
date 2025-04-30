package org.example.capstonedesign1.domain.cardproduct.repository;

import org.example.capstonedesign1.domain.cardproduct.dto.response.projection.CardProductRecommendationPreview;
import org.example.capstonedesign1.domain.cardproduct.entity.CardProductRecommendation;
import org.example.capstonedesign1.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CardProductRecommendationRepository extends JpaRepository<CardProductRecommendation, UUID> {

    @Query("select c.id as id, c.strategy as strategy, c.createdAt as createdAt " +
            "from CardProductRecommendation as c " +
            "where c.user = :user")
    Page<CardProductRecommendationPreview> findCardProductRecommendationsByUser(@Param(value = "user") User user,
                                                                                Pageable pageable);

}
