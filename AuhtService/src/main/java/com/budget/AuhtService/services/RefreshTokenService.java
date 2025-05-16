package com.budget.AuhtService.services;


import org.springframework.stereotype.Service;

@Service
public interface RefreshTokenService {

    String generateRefreshToken(Long userId);
    String refreshAccessToken(String refreshToken, Long userId);
    String revokeRefreshToken(String refreshToken, Long userId);
}
