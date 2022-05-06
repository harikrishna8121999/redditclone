package com.example.redditclone.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.redditclone.dto.RegisterRequest;
import com.example.redditclone.service.AuthService;

import lombok.AllArgsConstructor;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
	
	
	

	    private final AuthService authService;

	    @PostMapping("/signup")
	    public ResponseEntity signup(@RequestBody RegisterRequest registerRequest) {
	        authService.signup(registerRequest);
	        return new ResponseEntity(OK);
	    }
	    
	    @GetMapping("accountVerification/{token}")
	    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
	        authService.verifyAccount(token);
	        return new ResponseEntity<>("Account Activated Successully", OK);
	    }

}
