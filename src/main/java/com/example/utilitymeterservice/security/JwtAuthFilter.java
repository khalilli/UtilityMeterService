package com.example.utilitymeterservice.security;

import com.example.utilitymeterservice.dto.JwtUser;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthFilter implements Filter {
    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        ContentCachingRequestWrapper wrappedRequest =
                new ContentCachingRequestWrapper((HttpServletRequest) request, 1024 * 1024);

        HttpServletResponse res = (HttpServletResponse) response;
        String path = wrappedRequest.getRequestURI();

        // Allow public endpoints
        if (path.startsWith("/actuator")
                || path.startsWith("/swagger")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")) {
            chain.doFilter(wrappedRequest, response);
            return;
        }

        String authHeader = wrappedRequest.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                JwtUser user = jwtService.parseToken(token);
                wrappedRequest.setAttribute("user", user);
            } catch (Exception e) {
                throw new RuntimeException("Invalid or expired token");
            }
        } else {
            throw new RuntimeException("Missing Authorization header");
        }

        chain.doFilter(wrappedRequest, response);
    }
}
