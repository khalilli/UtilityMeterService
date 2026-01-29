package com.example.utilitymeterservice.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Cache configuration using Caffeine.
 *
 * <p>Settings are externalized to application properties with sensible defaults:
 * - cache.jwk-set.ttl-seconds (default: 3600)
 * - cache.default.maximum-size (default: 1000)
 * - cache.default.record-stats (default: true)
 */
@Configuration
@EnableCaching
public class CacheConfig {

    public static final String JWK_SET_CACHE = "jwkSet";

    @Value("${cache.jwk-set.ttl-seconds:3600}")
    private long jwkSetTtlSeconds;

    @Value("${cache.default.maximum-size:1000}")
    private long defaultMaxSize;

    @Value("${cache.default.record-stats:true}")
    private boolean recordStats;

    @Bean
    public CacheManager cacheManager() {
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                .expireAfterWrite(jwkSetTtlSeconds, TimeUnit.SECONDS)
                .maximumSize(defaultMaxSize);

        if (recordStats) {
            caffeine.recordStats();
        }

        CaffeineCacheManager cacheManager = new CaffeineCacheManager(JWK_SET_CACHE);
        cacheManager.setCaffeine(caffeine);
        return cacheManager;
    }
}