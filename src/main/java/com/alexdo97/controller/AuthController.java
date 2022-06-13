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
import com.alexdo97.service.IdentityService;
import com.alexdo97.service.JwtService;

@RestController
@CrossOrigin
public class AuthController {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private IdentityService identityService;

	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest) {
		return jwtService.createJwtToken(jwtRequest);
	}

	@PostMapping("/register")
	public ResponseEntity<Identity> register(@RequestBody Identity identity) {
		return identityService.createIdentity(identity);
	}

	@GetMapping("/identity")
	public ResponseEntity<Identity> getIdentity(@AuthenticationPrincipal User user) {
		return identityService.getIdentity(user);
	}
}
