package com.nuria.cvplatform.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;

    private static final String SECRET = "test_secret_key_min_32_chars_long_here";
    private static final long EXPIRATION = 900000L; // 15 min

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(SECRET, EXPIRATION);
    }

    @Test
    void generateToken_shouldReturnValidToken() {
        String token = jwtService.generateToken("admin_nuria", "ROLE_ADMIN");
        assertThat(token).isNotBlank();
    }

    @Test
    void extractUserId_shouldReturnCorrectUserId() {
        String token = jwtService.generateToken("admin_nuria", "ROLE_ADMIN");
        String userId = jwtService.extractUserId(token);
        assertThat(userId).isEqualTo("admin_nuria");
    }

    @Test
    void isTokenValid_shouldReturnTrue_whenTokenIsValid() {
        String token = jwtService.generateToken("admin_nuria", "ROLE_ADMIN");
        assertThat(jwtService.isTokenValid(token)).isTrue();
    }

    @Test
    void isTokenValid_shouldReturnFalse_whenTokenIsExpired() {
        JwtService expiredJwtService = new JwtService(SECRET, 0L);
        String token = expiredJwtService.generateToken("admin_nuria", "ROLE_ADMIN");
        assertThat(expiredJwtService.isTokenValid(token)).isFalse();
    }

    @Test
    void isTokenValid_shouldReturnFalse_whenTokenIsTampered() {
        String token = jwtService.generateToken("admin_nuria", "ROLE_ADMIN");
        String tamperedToken = token + "tampered";
        assertThat(jwtService.isTokenValid(tamperedToken)).isFalse();
    }
}