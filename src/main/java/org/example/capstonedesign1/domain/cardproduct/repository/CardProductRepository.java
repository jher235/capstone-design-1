package org.example.capstonedesign1.domain.cardproduct.repository;

import org.example.capstonedesign1.domain.cardproduct.entity.CardProduct;
import org.example.capstonedesign1.domain.propensity.entity.enums.Propensity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CardProductRepository extends JpaRepository<CardProduct, UUID> {

    @Query("select cp " +
            "from CardProduct cp " +
            "inner join CardProductPropensity cpp on cpp.cardProduct = cp and cpp.propensity = :propensity")
    List<CardProduct> getRecommendable(@Param("propensity") Propensity propensity);
}
