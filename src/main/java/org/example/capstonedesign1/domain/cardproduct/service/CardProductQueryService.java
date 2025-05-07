package org.example.capstonedesign1.domain.cardproduct.service;

import lombok.RequiredArgsConstructor;
import org.example.capstonedesign1.domain.cardproduct.dto.response.CardProductRecommendationResponse;
import org.example.capstonedesign1.domain.cardproduct.dto.response.projection.CardProductRecommendationPreview;
import org.example.capstonedesign1.domain.cardproduct.entity.CardProduct;
import org.example.capstonedesign1.domain.cardproduct.entity.CardProductRecommendation;
import org.example.capstonedesign1.domain.cardproduct.repository.CardProductRecommendationRepository;
import org.example.capstonedesign1.domain.cardproduct.repository.CardProductRepository;
import org.example.capstonedesign1.domain.user.entity.User;
import org.example.capstonedesign1.domain.user.service.UserQueryService;
import org.example.capstonedesign1.global.dto.PaginationResponse;
import org.example.capstonedesign1.global.exception.ForbiddenException;
import org.example.capstonedesign1.global.exception.NotFoundException;
import org.example.capstonedesign1.global.exception.code.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardProductQueryService {
    private final CardProductRepository cardProductRepository;
    private final CardProductRecommendationRepository cardProductRecommendationRepository;
    private final UserQueryService userQueryService;

    public CardProductRecommendation findRecommendationById(UUID recommendationId) {
        return cardProductRecommendationRepository.findById(recommendationId).orElseThrow(()
                -> new NotFoundException(ErrorCode.CARD_PRODUCT_RECOMMENDATION_NOT_FOUND));
    }

    public PaginationResponse<CardProductRecommendationPreview> getRecommendations(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<CardProductRecommendationPreview> cardProductRecommendations =
                cardProductRecommendationRepository.findCardProductRecommendationsByUser(user, pageable);
        return PaginationResponse.from(cardProductRecommendations);
    }

    public CardProductRecommendationResponse getRecommendation(User user, UUID recommendationId) {
        CardProductRecommendation recommendation = findRecommendationById(recommendationId);
        if (!userQueryService.isSameUser(user, recommendation.getUser())) {
            throw new ForbiddenException(ErrorCode.UN_AUTHORIZED);
        }
        return CardProductRecommendationResponse.from(recommendation);
    }

    public List<CardProduct> findByIds(List<UUID> cardProductIds) {
        return cardProductRepository.findAllById(cardProductIds);
    }

}
