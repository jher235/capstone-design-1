package org.example.capstonedesign1.domain.cardproduct.service;

import lombok.RequiredArgsConstructor;
import org.example.capstonedesign1.domain.cardproduct.dto.response.projection.CardProductRecommendationPreview;
import org.example.capstonedesign1.domain.cardproduct.repository.CardProductRecommendationRepository;
import org.example.capstonedesign1.domain.user.entity.User;
import org.example.capstonedesign1.global.dto.PaginationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardProductQueryService {
    private final CardProductRecommendationRepository cardProductRecommendationRepository;

    public PaginationResponse<CardProductRecommendationPreview> getRecommendations(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<CardProductRecommendationPreview> cardProductRecommendations =
                cardProductRecommendationRepository.findCardProductRecommendationsByUser(user, pageable);
        return PaginationResponse.from(cardProductRecommendations);
    }

}
