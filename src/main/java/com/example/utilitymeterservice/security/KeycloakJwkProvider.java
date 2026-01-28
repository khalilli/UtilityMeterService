package com.example.utilitymeterservice.security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

@Component
public class KeycloakJwkProvider {
    private final JWKSet jwkSet;

    public KeycloakJwkProvider() throws Exception {
        URL url = new URL(
                "http://localhost:8080/realms/utility-meter/protocol/openid-connect/certs"
        );
        this.jwkSet = JWKSet.load(url);
    }

    public RSAKey getKey(String kid) throws Exception {
        URL url = new URL("http://localhost:8080/realms/utility-meter/protocol/openid-connect/certs");
        JWKSet jwkSet = JWKSet.load(url);
        RSAKey rsaKey = (RSAKey) jwkSet.getKeyByKeyId(kid);
        if (rsaKey == null) throw new RuntimeException("Cannot find JWK for kid: " + kid);
        return rsaKey;
    }

}
