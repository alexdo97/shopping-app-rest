package com.alexdo97.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.alexdo97.model.Cart;
import com.alexdo97.model.Identity;
import com.alexdo97.model.Role;
import com.alexdo97.repository.IdentityRepository;
import com.alexdo97.repository.RoleRepository;
import com.alexdo97.util.JwtUtil;

@Service
public class IdentityService {

	private static final String TOKEN_PREFIX = "Bearer ";

	@Autowired
	IdentityRepository identityRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public ResponseEntity<Identity> register(@RequestBody Identity newIdentity) {
		List<Role> newRoleList = new ArrayList<>();
		newIdentity.getRoleList().forEach(role -> newRoleList.add(roleRepository.findById(role.getRoleName()).get()));
		newIdentity.setPassword(getEncodedPassword(newIdentity.getPassword()));
		newIdentity.setRoleList(newRoleList);
		newIdentity.getCustomer().setCart(new Cart());
		Identity identity = identityRepository.save(newIdentity);
		return ResponseEntity.ok(identity);
	}

	public ResponseEntity<Identity> getIdentity(String attribute) {
		String token = attribute.replace(TOKEN_PREFIX, "");
		String username = jwtUtil.getUsernameFromToken(token);
		Identity identity = identityRepository.findById(username).get();
		return ResponseEntity.ok(identity);
	}

	public String getEncodedPassword(String password) {
		return passwordEncoder.encode(password);
	}
}
