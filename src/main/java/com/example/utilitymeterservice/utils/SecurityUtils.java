package com.example.utilitymeterservice.utils;

import com.example.utilitymeterservice.dto.JwtUser;
import com.example.utilitymeterservice.exceptions.InvalidTokenException;
import com.example.utilitymeterservice.security.JwtAuthFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    /**
     * Extracts the authenticated user from the HTTP request.
     * Uses {@link JwtAuthFilter#USER_ATTRIBUTE} as the attribute key.
     *
     * @param request the HTTP servlet request
     * @return the authenticated JwtUser
     * @throws InvalidTokenException if user is not found in request
     */
    public JwtUser extractUser(HttpServletRequest request) {
        JwtUser user = (JwtUser) request.getAttribute(JwtAuthFilter.USER_ATTRIBUTE);
        if (user == null) {
            throw new InvalidTokenException("User information not available in request");
        }
        return user;
    }
}