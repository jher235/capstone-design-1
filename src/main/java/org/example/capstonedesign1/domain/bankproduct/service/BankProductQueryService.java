package org.example.capstonedesign1.domain.bankproduct.service;

import lombok.RequiredArgsConstructor;
import org.example.capstonedesign1.domain.bankproduct.dto.response.BankProductRecommendationResponse;
import org.example.capstonedesign1.domain.bankproduct.dto.response.projection.BankProductRecommendationPreview;
import org.example.capstonedesign1.domain.bankproduct.entity.BankProductRecommendation;
import org.example.capstonedesign1.domain.bankproduct.repository.BankProductRecommendationRepository;
import org.example.capstonedesign1.domain.user.entity.User;
import org.example.capstonedesign1.domain.user.service.UserQueryService;
import org.example.capstonedesign1.global.dto.PaginationResponse;
import org.example.capstonedesign1.global.exception.AuthorizedException;
import org.example.capstonedesign1.global.exception.NotFoundException;
import org.example.capstonedesign1.global.exception.code.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BankProductQueryService {
    private final BankProductRecommendationRepository bankProductRecommendationRepository;
    private final UserQueryService userQueryService;

    public PaginationResponse<BankProductRecommendationPreview> getRecommendations(User user, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<BankProductRecommendationPreview> bankProductRecommendations =
                bankProductRecommendationRepository.findBankProductRecommendationsByUser(user, pageable);

        return PaginationResponse.from(bankProductRecommendations);
    }

    public BankProductRecommendationResponse getRecommendation(User user, UUID recommendationId) {
        BankProductRecommendation recommendation = findById(recommendationId);
        if (!userQueryService.isSameUser(user, recommendation.getUser())) {
            throw new AuthorizedException(ErrorCode.UN_AUTHORIZED);
        }
        return BankProductRecommendationResponse.from(recommendation);
    }

    public BankProductRecommendation findById(UUID recommendationId) {
        return bankProductRecommendationRepository.findById(recommendationId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BANK_PRODUCT_RECOMMENDATION_NOT_FOUND));
    }
}
