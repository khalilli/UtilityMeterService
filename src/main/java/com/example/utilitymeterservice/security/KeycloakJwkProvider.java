package com.example.utilitymeterservice.security;

import com.example.utilitymeterservice.exceptions.InvalidTokenException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 * Provider responsible for obtaining the JWK Set. Uses {@link JwkFetcher} for cacheable
 * remote fetches and keeps a local fallback copy loaded at startup when possible.
 */
@Slf4j
@Component
public class KeycloakJwkProvider {

    private final JwkFetcher jwkFetcher;

    private volatile JWKSet cachedJwkSet;

    public KeycloakJwkProvider(JwkFetcher jwkFetcher, @Value("${app.security.jwt.jwk-set-uri}") String jwkSetUri) {
        this.jwkFetcher = jwkFetcher;
        try {
            // Attempt to prime a local cached value at startup
            var url = new URI(jwkSetUri).toURL();
            this.cachedJwkSet = JWKSet.load(url);
        } catch (Exception e) {
            log.warn("Failed to preload JWK set at startup; continuing without initial cache: {}", e.getMessage());
        }
    }

    public JWKSet getJwkSet() {
        // Use the cacheable fetcher (goes through Spring proxy)
        JWKSet fetched = jwkFetcher.fetch();
        if (fetched != null) {
            this.cachedJwkSet = fetched;
            return fetched;
        }
        if (cachedJwkSet != null) {
            return cachedJwkSet;
        }
        throw new InvalidTokenException("Cannot load JWK set and no cache available");
    }

    public RSAKey getKey(String kid) {
        JWKSet jwkSet = getJwkSet();
        var key = jwkSet.getKeyByKeyId(kid);
        if (!(key instanceof RSAKey)) {
            throw new InvalidTokenException("Cannot find RSA JWK for kid: " + kid);
        }
        return (RSAKey) key;
    }

}