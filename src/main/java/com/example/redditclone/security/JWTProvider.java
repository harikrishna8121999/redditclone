package com.example.redditclone.security;

import java.io.IOException;

import static io.jsonwebtoken.Jwts.parser;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.redditclone.exception.SpringRedditException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import org.springframework.security.core.userdetails.User;
import static java.util.Date.from;

@Service
public class JWTProvider {

	  private KeyStore keyStore;
	  @Value("${jwt.expiration.time}")
	  private Long jwtExpirationInMillis;

	    @PostConstruct
	    public void init() {
	        try {
	            keyStore = KeyStore.getInstance("JKS");
	            InputStream resourceAsStream = getClass().getResourceAsStream("/server-app.jks");
	            keyStore.load(resourceAsStream, "123456".toCharArray());
	        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
	            throw new SpringRedditException("Exception occurred while loading keystore");
	        }

	    }

	    public String generateToken(Authentication authentication) {
	        org.springframework.security.core.userdetails.User principal = (User) authentication.getPrincipal();
	        return Jwts.builder()
	                .setSubject(principal.getUsername())
	                .signWith(getPrivateKey())
	                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
	                .compact();
	    }
	    
	    public String generateTokenWithUserName(String username) {
	        return Jwts.builder()
	                .setSubject(username)
	                .setIssuedAt(from(Instant.now()))
	                .signWith(getPrivateKey())
	                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
	                .compact();
	    }

	    private PrivateKey getPrivateKey() {
	        try {
	            return (PrivateKey) keyStore.getKey("server-app", "123456".toCharArray());
	        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
	            throw new SpringRedditException("Exception occured while retrieving public key from keystore");
	        }
	    }
	    
	    public boolean validateToken(String jwt) {
	        parser().setSigningKey(getPublickey()).parseClaimsJws(jwt);
	        return true;
	    }
	    
	    private PublicKey getPublickey() {
	        try {
	            return keyStore.getCertificate("server-app").getPublicKey();
	        } catch (KeyStoreException e) {
	            throw new SpringRedditException("Exception occured while retrieving public key from keystore");
	        }
	    }
	    
	    public String getUsernameFromJWT(String token) {
	        Claims claims = parser()
	                .setSigningKey(getPublickey())
	                .parseClaimsJws(token)
	                .getBody();

	        return claims.getSubject();
	    }
	    
	    public Long getJwtExpirationInMillis() {
	        return jwtExpirationInMillis;
	    }
}
