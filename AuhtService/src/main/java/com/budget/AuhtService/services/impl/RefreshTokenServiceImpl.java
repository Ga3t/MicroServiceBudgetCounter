package com.budget.AuhtService.services.impl;


import com.budget.AuhtService.models.RefreshToken;
import com.budget.AuhtService.models.UserEntity;
import com.budget.AuhtService.repository.RefreshTokenRepository;
import com.budget.AuhtService.repository.UserRepository;
import com.budget.AuhtService.services.RefreshTokenService;
import jakarta.ws.rs.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

@Primary
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${app.jwt.refreshDuration}")
    private Duration refreshTokenDuration;

    private RefreshTokenRepository refreshTokenRepository;
    private UserRepository userRepository;

    @Autowired
    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String generateRefreshToken(Long userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));
        RefreshToken refreshTokenEntity =new RefreshToken();
        refreshTokenEntity.setCreatedAt(Instant.now());
        refreshTokenEntity.setExpiryDate(Instant.now().plus(refreshTokenDuration));
        refreshTokenEntity.setUser(user);

        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[64];
        random.nextBytes(bytes);
        String refreshToken = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        refreshTokenEntity.setToken(refreshToken);

        refreshTokenRepository.save(refreshTokenEntity);
        return refreshToken;
    }

    @Override
    public String refreshAccessToken(String refreshToken, Long userId) {
        return "";
    }

    @Override
    public String revokeRefreshToken(String refreshToken, Long userId) {

        RefreshToken revokeToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(()->new RuntimeException("No such token"));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));
        if(!user.getId().equals(revokeToken.getUser().getId()))
            throw new ForbiddenException("Leaked token");

        revokeToken.setRevoked(true);
        refreshTokenRepository.save(revokeToken);

        return "Token was revoked";
    }
}
