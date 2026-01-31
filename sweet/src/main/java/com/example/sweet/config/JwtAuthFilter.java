package com.example.sweet.config;

import com.example.sweet.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // ==========================
    // ✅ SKIP JWT FOR PUBLIC APIs
    // ==========================
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getServletPath();
        String method = request.getMethod();

        // 🔥 Allow CORS preflight
        if ("OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }

        // 🔓 Public auth routes
        if (path.startsWith("/api/auth")) {
            return true;
        }

        // 🔓 Public GET APIs (React + Android)
        if ("GET".equalsIgnoreCase(method) &&
                (path.startsWith("/api/hero")
                        || path.startsWith("/api/categories")
                        || path.startsWith("/api/sweets"))) {
            return true;
        }

        return false;
    }

    // ==========================
    // ✅ JWT VALIDATION
    // ==========================
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            try {
                if (!jwtUtil.isTokenValid(token)) {
                    filterChain.doFilter(request, response);
                    return;
                }

                String email = jwtUtil.extractEmail(token);
                String role = jwtUtil.extractRole(token);

                String normalizedRole =
                        role.startsWith("ROLE_") ? role : "ROLE_" + role;

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                Collections.singleton(() -> normalizedRole)
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

            } catch (Exception ignored) {
                // Leave context empty → Spring Security will block protected routes
            }
        }

        filterChain.doFilter(request, response);
    }
}
