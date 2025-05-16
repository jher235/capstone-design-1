package org.example.capstonedesign1.domain.cardproduct.repository;

import java.util.List;
import java.util.UUID;

import org.example.capstonedesign1.domain.cardproduct.entity.CardProduct;
import org.example.capstonedesign1.domain.cardproduct.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardProductRepository extends JpaRepository<CardProduct, UUID> {

	@Query("""
		select distinct cp
		from CardProduct cp
		inner join CardProductCategory cpc
		where  cpc.category in :categories
		group by cp.id
		order by count(cpc)
		""")
	List<CardProduct> findByCategoryList(@Param("categories") List<Category> categories);

}
