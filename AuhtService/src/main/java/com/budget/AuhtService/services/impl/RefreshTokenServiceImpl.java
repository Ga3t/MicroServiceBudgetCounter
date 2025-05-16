package com.budget.AuhtService.services.impl;


import com.budget.AuhtService.dto.AuthResponseDto;
import com.budget.AuhtService.exceptions.RefreshTokenExpiredException;
import com.budget.AuhtService.exceptions.RefreshTokenLeakedException;
import com.budget.AuhtService.models.RefreshToken;
import com.budget.AuhtService.models.UserEntity;
import com.budget.AuhtService.repository.RefreshTokenRepository;
import com.budget.AuhtService.repository.UserRepository;
import com.budget.AuhtService.services.RefreshTokenService;
import com.budget.core.exceptions.TokenNotFoundException;
import com.budget.core.exceptions.UserNotFoundException;
import com.budget.core.security.JwtGenerator;
import jakarta.ws.rs.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.security.SecureRandom;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.List;

@Primary
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${app.jwt.refreshDuration}")
    private Duration refreshTokenDuration;


    private AuthenticationManager authenticationManager;
    private RefreshTokenRepository refreshTokenRepository;
    private UserRepository userRepository;
    private JwtGenerator jwtGenerator;

    @Autowired
    public RefreshTokenServiceImpl(AuthenticationManager authenticationManager, RefreshTokenRepository refreshTokenRepository, UserRepository userRepository, JwtGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.jwtGenerator = jwtGenerator;
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
    public AuthResponseDto refreshAccessToken(String refreshToken, Long userId) {

        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(()->new TokenNotFoundException("No such  refresh token"));

        Instant expiryDate = token.getExpiryDate();

        if(expiryDate.isBefore(Instant.now()))
            throw new RefreshTokenExpiredException("Token was expired need to login again");

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User not found"));

        if(!user.getId().equals(token.getUser().getId()))
            throw new RefreshTokenLeakedException(token.getId());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                null,
                List.of(new SimpleGrantedAuthority(user.getRole().name()))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtGenerator.generateAccessToken(authentication, userId, user.getRole());
        String newRefreshToken= generateRefreshToken(userId);
        revokeRefreshToken(token.getToken(), userId);

        return new AuthResponseDto(jwtToken, newRefreshToken);
    }

    @Override
    public void revokeRefreshToken(String refreshToken, Long userId) {

        RefreshToken revokeToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(()->new TokenNotFoundException("No such refresh token"));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User not found"));
        if(!user.getId().equals(revokeToken.getUser().getId()))
            throw new ForbiddenException("Leaked token");

        revokeToken.setRevoked(true);
        refreshTokenRepository.save(revokeToken);
    }
}
