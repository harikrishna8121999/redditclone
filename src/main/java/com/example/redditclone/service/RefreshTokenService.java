package com.example.redditclone.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.redditclone.exception.SpringRedditException;
import com.example.redditclone.model.RefreshToken;
import com.example.redditclone.repository.RefreshTokenRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {
	
	 private final RefreshTokenRepository refreshTokenRepository;

	    RefreshToken generateRefreshToken() {
	        RefreshToken refreshToken = new RefreshToken();
	        refreshToken.setToken(UUID.randomUUID().toString());
	        refreshToken.setCreatedDate(Instant.now());

	        return refreshTokenRepository.save(refreshToken);
	    }

	    void validateRefreshToken(String token) {
	        refreshTokenRepository.findByToken(token)
	                .orElseThrow(() -> new SpringRedditException("Invalid refresh Token"));
	    }

	    public void deleteRefreshToken(String token) {
	        refreshTokenRepository.deleteByToken(token);
	    }

}
