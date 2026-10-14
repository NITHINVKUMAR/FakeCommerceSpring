package com.example.FakeCommerce.services.cache;

import com.example.FakeCommerce.dto.GetReviewResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewRedisCache {
    private static final String KEY_PREFIX = "review:";
    private static final Duration CACHE_TTL = Duration.ofMinutes(5);

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public Optional<GetReviewResponseDto> getReview(Long id) {
        String json = stringRedisTemplate.opsForValue().get(KEY_PREFIX + id);

        if (json == null) {
            log.info("Cache miss for review: {}", id);
            return Optional.empty();
        }

        log.info("Cache hit for review: {}", id);
        try {
            GetReviewResponseDto review = objectMapper.readValue(json, GetReviewResponseDto.class);
            return Optional.of(review);
        } catch (Exception e) {
            log.error("Error parsing review from cache: {}", e.getMessage());
            stringRedisTemplate.delete(KEY_PREFIX + id); // Clean up corrupted data
            return Optional.empty();
        }
    }

    public void putReview(Long id, GetReviewResponseDto review) {
        try {
            stringRedisTemplate.opsForValue().set(
                    KEY_PREFIX + id,
                    objectMapper.writeValueAsString(review),
                    CACHE_TTL
            );
        } catch (Exception e) {
            log.error("Error serializing review to cache: {}", e.getMessage());
        }
    }

    public void evictReview(Long id) {
        stringRedisTemplate.delete(KEY_PREFIX + id);
        log.info("Evicted review from cache: {}", id);
    }
}