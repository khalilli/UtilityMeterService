package com.example.utilitymeterservice.dto;

import java.util.Set;

public record JwtUser (
        String userId,
        String email,
        Set<String> roles
) { }
