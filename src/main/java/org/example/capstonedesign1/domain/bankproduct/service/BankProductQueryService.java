package org.example.capstonedesign1.domain.bankproduct.service;

import lombok.RequiredArgsConstructor;
import org.example.capstonedesign1.domain.bankproduct.dto.response.projection.BankProductRecommendationPreview;
import org.example.capstonedesign1.domain.bankproduct.entity.BankProductRecommendation;
import org.example.capstonedesign1.domain.bankproduct.repository.BankProductRecommendationRepository;
import org.example.capstonedesign1.domain.user.entity.User;
import org.example.capstonedesign1.global.dto.PaginationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankProductQueryService {
    private final BankProductRecommendationRepository bankProductRecommendationRepository;

    public PaginationResponse<BankProductRecommendationPreview> getRecommendations(User user, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<BankProductRecommendationPreview> bankProductRecommendations =
                bankProductRecommendationRepository.findBankProductRecommendationsByUser(user, pageable);

        return PaginationResponse.from(bankProductRecommendations);
    }
}
