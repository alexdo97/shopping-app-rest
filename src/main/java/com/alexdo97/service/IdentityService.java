package com.alexdo97.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.alexdo97.model.Cart;
import com.alexdo97.model.Identity;
import com.alexdo97.model.Role;
import com.alexdo97.repository.IdentityRepository;
import com.alexdo97.repository.RoleRepository;

@Service
public class IdentityService {

	@Autowired
	IdentityRepository identityRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public Identity register(@RequestBody Identity identity) {
		List<Role> newRoles = new ArrayList<>();
		identity.getRoles().forEach(role -> newRoles.add(roleRepository.findById(role.getRoleName()).get()));
		identity.setPassword(getEncodedPassword(identity.getPassword()));
		identity.setRoles(newRoles);
		identity.getCustomer().setCart(new Cart());
		return identityRepository.save(identity);
	}

	public String getEncodedPassword(String password) {
		return passwordEncoder.encode(password);
	}
}
