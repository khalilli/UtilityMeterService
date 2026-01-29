package com.example.utilitymeterservice.security;

import com.example.utilitymeterservice.dto.JwtUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * Servlet filter that validates JWTs on incoming requests and attaches a parsed
 * {@link JwtUser} to the request attributes for downstream handlers.
 */

@Slf4j
@Component
public class JwtAuthFilter implements Filter {
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    public static final String USER_ATTRIBUTE = "user";

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    public JwtAuthFilter(JwtService jwtService, ObjectProvider<ObjectMapper> objectMapperProvider) {
        this.jwtService = jwtService;
        // Use provided ObjectMapper bean if available; otherwise create a default one
        this.objectMapper = objectMapperProvider.getIfAvailable(ObjectMapper::new);
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
        if (isPublicEndpoint(path)) {
            chain.doFilter(wrappedRequest, response);
            return;
        }

        String authHeader = wrappedRequest.getHeader(AUTH_HEADER);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            sendUnauthorizedResponse(res, "Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());
        try {
            JwtUser user = jwtService.parseToken(token);
            if (user == null) {
                sendUnauthorizedResponse(res, "Failed to parse token");
                return;
            }
            wrappedRequest.setAttribute(USER_ATTRIBUTE, user);
            chain.doFilter(wrappedRequest, response);
        } catch (com.example.utilitymeterservice.exceptions.InvalidTokenException e) {
            // Known validation error: return 401 and provide a clear message
            log.warn("Authentication failed: {}", e.getMessage());
            sendUnauthorizedResponse(res, e.getMessage());
        } catch (Exception e) {
            // Unexpected error: log full stacktrace and return a generic message
            log.error("Unexpected error during authentication", e);
            sendUnauthorizedResponse(res, "Authentication error");
        }
    }

    private boolean isPublicEndpoint(String path) {
        return path.startsWith("/actuator/health")
                || path.startsWith("/actuator/info")
                || path.startsWith("/swagger")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs");
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> body = new HashMap<>();
        body.put("status", 401);
        body.put("error", "Unauthorized");
        body.put("message", message);

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}