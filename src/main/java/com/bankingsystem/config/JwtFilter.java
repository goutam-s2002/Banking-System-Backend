package com.bankingsystem.config;

import java.io.IOException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.bankingsystem.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    // Added: SLF4J Logger (30-06-2026)
    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        log.info("JWT Filter | Path={}", req.getServletPath());

        String path = req.getServletPath();

        // Modified: Skip Public APIs (30-06-2026)
        if (path.equals("/auth/login")
                || path.equals("/auth/register")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")) {

            log.debug("Public API - JWT Skipped");

            chain.doFilter(request, response);
            return;
        }

        String header = req.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {

            try {

                String token = header.substring(7);

                String username = jwtUtil.extractUsername(token);
                String role = jwtUtil.extractRole(token);

                log.info("Authenticated User={} Role={}", username, role);

                if (username != null &&
                        SecurityContextHolder.getContext().getAuthentication() == null) {

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    Collections.singleton(() -> "ROLE_" + role)
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(req));

                    SecurityContextHolder.getContext()
                            .setAuthentication(authToken);

                    log.debug("Authentication stored in SecurityContext");
                }

            } catch (Exception e) {

                log.error("JWT Validation Failed : {}", e.getMessage());
            }

        } else {

            log.warn("Authorization Header Missing");
        }

        chain.doFilter(request, response);
    }

}