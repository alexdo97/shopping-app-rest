package com.alexdo97.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alexdo97.model.Identity;
import com.alexdo97.model.JwtRequest;
import com.alexdo97.model.JwtResponse;
import com.alexdo97.service.AuthService;

@RestController
@CrossOrigin
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest) {
		return authService.createJwtToken(jwtRequest);
	}

	@PostMapping("/register")
	public ResponseEntity<Identity> register(@RequestBody Identity identity) {
		return authService.createIdentity(identity);
	}

	@GetMapping("/identity")
	public ResponseEntity<Identity> getIdentity(@AuthenticationPrincipal User user) {
		return authService.getIdentity(user);
	}
}
