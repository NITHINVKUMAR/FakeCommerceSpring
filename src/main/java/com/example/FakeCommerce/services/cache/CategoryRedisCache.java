package com.example.FakeCommerce.services.cache;

import com.example.FakeCommerce.schema.Category;
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

public class CategoryRedisCache {
    private static final String KEY_PREFIX = "category:";
    private static final Duration CACHE_TTL = Duration.ofMinutes(5);

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public Optional<Category> getCategory(Long id) {
        String json = stringRedisTemplate.opsForValue().get(KEY_PREFIX + id);

        if (json == null) {
            log.info("Cache miss for category: {}", id);
            return Optional.empty();
        }

        log.info("Cache hit for category: {}", id);
        try {
            Category category = objectMapper.readValue(json, Category.class);
            return Optional.of(category);
        } catch (Exception e) {
            log.error("Error parsing category from cache: {}", e.getMessage());
            stringRedisTemplate.delete(KEY_PREFIX + id); // Clean up corrupted data
            return Optional.empty();
        }
    }

    public void putCategory(Long id, Category category) {
        try {
            stringRedisTemplate.opsForValue().set(
                    KEY_PREFIX + id,
                    objectMapper.writeValueAsString(category),
                    CACHE_TTL
            );
        } catch (Exception e) {
            log.error("Error serializing category to cache: {}", e.getMessage());
        }
    }

    public void evictCategory(Long id) {
        stringRedisTemplate.delete(KEY_PREFIX + id);
        log.info("Evicted category from cache: {}", id);
    }
}
