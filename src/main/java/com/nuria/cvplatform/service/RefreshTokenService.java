package com.nuria.cvplatform.service;

import com.nuria.cvplatform.model.RefreshToken;
import com.nuria.cvplatform.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository repository;

    private final long refreshDurationDays = 7;

    public RefreshToken createRefreshToken(String userId) {
        RefreshToken token = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .userId(userId)
                .expiryDate(LocalDateTime.now().plusDays(refreshDurationDays))
                .revoked(false)
                .build();

        return repository.save(token);
    }

    public RefreshToken verifyToken(String token) {

        RefreshToken refreshToken = repository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.isRevoked())
            throw new RuntimeException("Refresh token revoked");

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Refresh token expired");

        return refreshToken;
    }

    public void revokeToken(String token) {
        repository.findByToken(token).ifPresent(t -> {
            t.setRevoked(true);
            repository.save(t);
        });
    }

    public void revokeAllUserTokens(String userId) {
        repository.deleteByUserId(userId);
    }
}
