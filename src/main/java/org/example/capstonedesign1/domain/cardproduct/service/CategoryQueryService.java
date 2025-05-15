package org.example.capstonedesign1.domain.cardproduct.service;

import java.util.List;

import org.example.capstonedesign1.domain.cardproduct.entity.Category;
import org.example.capstonedesign1.domain.cardproduct.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryQueryService {

	private final CategoryRepository categoryRepository;

	public List<Category> getAllCategory() {
		return categoryRepository.findAll();
	}
}
