package com.example.utilitymeterservice.security;

import com.example.utilitymeterservice.config.CacheConfig;
import com.nimbusds.jose.jwk.JWKSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 * Fetches the JWK Set from Keycloak and caches it using Spring's Cache abstraction.
 */
@Slf4j
@Component
public class JwkFetcher {

    private final String jwkSetUri;

    public JwkFetcher(@Value("${app.security.jwt.jwk-set-uri}") String jwkSetUri) {
        this.jwkSetUri = jwkSetUri;
    }

    @Cacheable(value = CacheConfig.JWK_SET_CACHE, unless = "#result == null")
    public JWKSet fetch() {
        try {
            var url = new URI(jwkSetUri).toURL();
            return JWKSet.load(url);
        } catch (Exception e) {
            log.warn("Failed to fetch JWK set from {}: {}", jwkSetUri, e.getMessage());
            return null;
        }
    }
}