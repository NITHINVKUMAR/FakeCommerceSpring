package com.example.FakeCommerce.services;

import com.example.FakeCommerce.adapters.ReviewAdapter;
import com.example.FakeCommerce.dto.GetReviewResponseDto;
import com.example.FakeCommerce.exceptions.ResourceNotFoundException;
import com.example.FakeCommerce.repositories.ReviewRepository;
import com.example.FakeCommerce.schema.Review;
import com.example.FakeCommerce.services.cache.ReviewRedisCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewAdapter reviewAdapter;
    private final ReviewRedisCache reviewRedisCache;

    public List<GetReviewResponseDto> getAllReviews() {
        return reviewAdapter.mapToGetReviewResponseDtoList(reviewRepository.findAll());
    }

    public GetReviewResponseDto getReviewById(Long id) {
        // 1. Check cache first
        Optional<GetReviewResponseDto> cachedReview = reviewRedisCache.getReview(id);
        if (cachedReview.isPresent()) {
            return cachedReview.get();
        }

        // 2. Fetch from DB and map to DTO
        GetReviewResponseDto reviewDto = reviewRepository.findById(id)
                .map(reviewAdapter::mapToGetReviewResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Review with id " + id + " not found"));

        // 3. Put into cache
        reviewRedisCache.putReview(id, reviewDto);

        return reviewDto;
    }

    public List<GetReviewResponseDto> getReviewsByProductId(Long productId) {
        return reviewAdapter.mapToGetReviewResponseDtoList(reviewRepository.findByProductsId(productId));
    }

    public List<GetReviewResponseDto> getReviewsByOrderId(Long orderId) {
        return reviewAdapter.mapToGetReviewResponseDtoList(reviewRepository.findByOrderId(orderId));
    }

    public Review createReview(Review review) {
        Review savedReview = reviewRepository.save(review);
        // Cache the newly created review mapping right away
        GetReviewResponseDto reviewDto = reviewAdapter.mapToGetReviewResponseDto(savedReview);
        reviewRedisCache.putReview(savedReview.getId(), reviewDto);
        return savedReview;
    }

    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review with id " + id + " not found"));
        reviewRepository.delete(review);

        // Evict from cache
        reviewRedisCache.evictReview(id);
    }
}