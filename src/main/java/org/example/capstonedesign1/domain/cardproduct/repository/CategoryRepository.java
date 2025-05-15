package org.example.capstonedesign1.domain.cardproduct.repository;

import org.example.capstonedesign1.domain.cardproduct.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
