package com.example.FakeCommerce.services.cache;

import com.example.FakeCommerce.dto.GetOrderSummaryResponseDto;
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
public class OrderRedisCache {
    private static final String KEY_SUMMARY = "order:summary:";
    private static final Duration CACHE_TTL = Duration.ofMinutes(3);

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public Optional<GetOrderSummaryResponseDto> getOrderSummary(Long id) {
        String json = stringRedisTemplate.opsForValue().get(KEY_SUMMARY + id);

        if (json == null) {
            log.info("Cache miss for order summary: {}", id);
            return Optional.empty();
        }

        log.info("Cache hit for order summary: {}", id);
        try {
            GetOrderSummaryResponseDto summary = objectMapper.readValue(json, GetOrderSummaryResponseDto.class);
            return Optional.of(summary);
        } catch (Exception e) {
            log.error("Error parsing order summary from cache: {}", e.getMessage());
            stringRedisTemplate.delete(KEY_SUMMARY + id); // Clean up corrupted data
            return Optional.empty();
        }
    }

    public void putOrderSummary(Long id, GetOrderSummaryResponseDto summary) {
        try {
            stringRedisTemplate.opsForValue().set(
                    KEY_SUMMARY + id,
                    objectMapper.writeValueAsString(summary),
                    CACHE_TTL
            );
        } catch (Exception e) {
            log.error("Error serializing order summary to cache: {}", e.getMessage());
        }
    }

    public void evictOrderSummary(Long id) {
        stringRedisTemplate.delete(KEY_SUMMARY + id);
        log.info("Evicted order summary from cache: {}", id);
    }
}