package com.example.chaeum.chaeum_be.jwt;

import com.example.chaeum.chaeum_be.code.ErrorCode;
import com.example.chaeum.chaeum_be.entity.User;
import com.example.chaeum.chaeum_be.exception.GlobalException;
import com.example.chaeum.chaeum_be.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    private static final List<String> NO_AUTH_URLS = List.of(
            "/login",
            "/register",
            "/v3/api-docs",
            "/swagger-ui",
            "/swagger-resources",
            "/webjars"
    );

    public JWTFilter(JWTUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return NO_AUTH_URLS.stream().anyMatch(url -> path.startsWith(url));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        filterChain.doFilter(request, response);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()
                && !"anonymousUser".equals(auth.getPrincipal())) {

            String email = auth.getName(); // principal에서 username/email 가져오기
            String token = jwtUtil.createJWT(email); // JWTUtil에 토큰 생성 메소드 필요

            // 모든 응답 헤더에 JWT 넣기
            response.setHeader("Authorization", "Bearer " + token);
        }

        String authorization = request.getHeader("Authorization");
        log.info("Authorization header: {}", authorization);
        log.info("Request URI: {}", request.getRequestURI());

        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            try {
                String email = jwtUtil.validationJWT(token);
                User user = userRepository.findUserByEmail(email)
                        .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));

                var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(user, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authToken);

            } catch (Exception e) {
                log.error("Invalid JWT Token", e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } else {
            log.info("No JWT token found in request headers");
        }

    }
}