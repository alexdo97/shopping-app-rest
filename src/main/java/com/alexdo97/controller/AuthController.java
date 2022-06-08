package com.alexdo97.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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
	public JwtResponse login(@RequestBody JwtRequest jwtRequest) throws Exception {
		return jwtService.createJwtToken(jwtRequest);
	}

	@PostMapping("/register")
	public Identity register(@RequestBody Identity identity) {
		return identityService.register(identity);
	}
}
