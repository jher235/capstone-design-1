package org.example.capstonedesign1.domain.bankproduct.repository;

import org.example.capstonedesign1.domain.bankproduct.entity.BankProduct;
import org.example.capstonedesign1.domain.propensity.entity.enums.Propensity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BankProductRepository extends JpaRepository<BankProduct, UUID> {
    @Query("select bp " +
            "from BankProduct bp " +
            "inner join BankProductPropensity bpp on bp = bpp.bankProduct and bpp.propensity = :propensity " +
            "where bp.minAmount <= :amount and bp.minTerm <= :term ")
    List<BankProduct> findRecommendable(@Param("propensity") Propensity propensity,
                                        @Param("amount") Long amount,
                                        @Param("term") Integer term);
}
