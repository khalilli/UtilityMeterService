package com.example.utilitymeterservice.security;

import com.example.utilitymeterservice.dto.JwtUser;
import com.example.utilitymeterservice.exceptions.InvalidTokenException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * JWT handling: parsing, verification and mapping to application JwtUser.
 */
@Slf4j
@Service
public class JwtService {
    private final KeycloakJwkProvider jwkProvider;

    @Value("${app.security.jwt.issuer-uri}")
    private String expectedIssuer;

    @Value("${keycloak.resource}")
    private String expectedClient;

    public JwtService(KeycloakJwkProvider jwkProvider) {
        this.jwkProvider = jwkProvider;
    }

    /**
     * Parse and validate a signed JWT, returning the mapped {@link JwtUser}.
     * Throws {@link InvalidTokenException} for any validation or parsing errors.
     */
    public JwtUser parseToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            String keyId = signedJWT.getHeader().getKeyID();
            if (keyId == null) {
                throw new InvalidTokenException("Token missing key ID");
            }

            RSAKey rsaKey = jwkProvider.getKey(keyId);

            JWSVerifier verifier = new RSASSAVerifier(rsaKey.toRSAPublicKey());

            if (!signedJWT.verify(verifier)) {
                throw new InvalidTokenException("Invalid JWT signature");
            }

            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            log.debug("Token validation started");

            // Check expiration
            Date now = new Date();
            if (claims.getExpirationTime() == null || claims.getExpirationTime().before(now)) {
                throw new InvalidTokenException("Token expired");
            }

            // Check issuer
            if (claims.getIssuer() == null || !claims.getIssuer().equals(expectedIssuer)) {
                throw new InvalidTokenException("Invalid token issuer");
            }

            // Check client (azp = authorized party)
            String azp = null;
            try {
                azp = claims.getStringClaim("azp");
            } catch (ParseException ignored) {
                // will be handled by null check below
            }
            if (azp == null || !azp.equals(expectedClient)) {
                throw new InvalidTokenException("Token not issued for this client");
            }

            String subject = claims.getSubject();
            if (subject == null) {
                throw new InvalidTokenException("Token missing subject claim");
            }

            log.debug("Token validation successful");

            return new JwtUser(
                    subject,
                    claims.getStringClaim("email"),
                    extractRoles(claims)
            );

        } catch (InvalidTokenException e) {
            throw e;
        } catch (ParseException | JOSEException e) {
            log.warn("Token parsing failed: {}", e.getMessage());
            throw new InvalidTokenException("Invalid or malformed token", e);
        } catch (Exception e) {
            log.error("Unexpected error during token validation", e);
            throw new InvalidTokenException("Token validation failed", e);
        }
    }

    private Set<String> extractRoles(JWTClaimsSet claims) {
        try {
            var realmAccessObj = claims.getClaim("realm_access");
            if (!(realmAccessObj instanceof Map<?, ?>)) return Set.of();

            Map<?, ?> realmAccess = (Map<?, ?>) realmAccessObj;

            Object rolesObj = realmAccess.get("roles");
            if (rolesObj instanceof Collection<?>) {
                Set<String> roles = new HashSet<>();
                for (Object o : (Collection<?>) rolesObj) {
                    if (o instanceof String) roles.add((String) o);
                }
                return roles;
            }
            return Set.of();
        } catch (Exception e) {
            log.warn("Failed to extract roles from token: {}", e.getMessage());
            return Set.of();
        }
    }

    /**
     * Extracts the JwtUser placed into the request by {@link JwtAuthFilter}.
     * Throws {@link InvalidTokenException} when not present.
     */
    public JwtUser extractUser(HttpServletRequest request) {
        JwtUser user = (JwtUser) request.getAttribute(JwtAuthFilter.USER_ATTRIBUTE);
        if (user == null) {
            throw new InvalidTokenException("User information not available in request");
        }
        return user;
    }
}