package org.example.capstonedesign1.domain.bankproduct.repository;

import java.util.List;
import java.util.UUID;

import org.example.capstonedesign1.domain.bankproduct.entity.BankProduct;
import org.example.capstonedesign1.domain.propensity.entity.enums.Propensity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BankProductRepository extends JpaRepository<BankProduct, UUID> {

	@Query("""
		select bp
		from BankProduct bp
		where bp.propensity = :propensity
		and (bp.minAmount <= :amount or bp.minAmount is null )
		and (bp.minTerm <= :term or bp.minTerm is null)
		""")
	List<BankProduct> findRecommendable(@Param("propensity") Propensity propensity,
		@Param("amount") Long amount,
		@Param("term") Integer term);
}
