package com.example.redditclone.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.annotation.PostConstruct;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.redditclone.exception.SpringRedditException;

import io.jsonwebtoken.Jwts;

import org.springframework.security.core.userdetails.User;

@Service
public class JWTProvider {

	  private KeyStore keyStore;

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
	                .compact();
	    }

	    private PrivateKey getPrivateKey() {
	        try {
	            return (PrivateKey) keyStore.getKey("server-app", "123456".toCharArray());
	        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
	            throw new SpringRedditException("Exception occured while retrieving public key from keystore");
	        }
	    }
}
