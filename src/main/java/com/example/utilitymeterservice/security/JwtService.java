package com.example.utilitymeterservice.security;

import com.example.utilitymeterservice.dto.JwtUser;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@Slf4j
@Service
public class JwtService {
    private final KeycloakJwkProvider jwkProvider;

    public JwtService(KeycloakJwkProvider jwkProvider) {
        this.jwkProvider = jwkProvider;
    }

    public JwtUser parseToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            String keyId = signedJWT.getHeader().getKeyID();
            RSAKey rsaKey = jwkProvider.getKey(keyId); // fetch fresh key

            JWSVerifier verifier = new RSASSAVerifier(rsaKey.toRSAPublicKey());

            if (!signedJWT.verify(verifier)) {
                throw new RuntimeException("Invalid JWT signature");
            }

            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            log.info("Token header: {}", signedJWT.getHeader());
            log.info("Token claims: {}", claims.toJSONObject());

            // Check expiration
            Date now = new Date();
            if (claims.getExpirationTime() == null || claims.getExpirationTime().before(now)) {
                throw new RuntimeException("Token expired");
            }

            // Check issuer
            String expectedIssuer = "http://localhost:8080/realms/utility-meter";
            if (!expectedIssuer.equals(claims.getIssuer())) {
                throw new RuntimeException("Invalid token issuer");
            }

            // Check client (azp = authorized party)
            String azp = claims.getStringClaim("azp");
            if (!"meter-api".equals(azp)) {
                throw new RuntimeException("Token not issued for this client");
            }

            return new JwtUser(
                    claims.getSubject(),
                    claims.getStringClaim("email"),
                    extractRoles(claims)
            );

        } catch (ParseException | JOSEException | IOException e) {
            throw new RuntimeException("Invalid token", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Set<String> extractRoles(JWTClaimsSet claims) {
        Map<String, Object> realmAccess =
                (Map<String, Object>) claims.getClaim("realm_access");

        if (realmAccess == null) return Set.of();

        return new HashSet<>(
                (List<String>) realmAccess.get("roles")
        );
    }
}
