package com.alexdo97.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.alexdo97.model.Identity;
import com.alexdo97.model.JwtRequest;
import com.alexdo97.model.JwtResponse;
import com.alexdo97.repository.IdentityRepository;
import com.alexdo97.util.JwtUtil;

@Service
public class JwtService implements UserDetailsService {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private IdentityRepository identityRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	public ResponseEntity<JwtResponse> createJwtToken(JwtRequest jwtRequest) throws Exception {
		String userName = jwtRequest.getUserName();
		String userPassword = jwtRequest.getUserPassword();
		authenticate(userName, userPassword);

		UserDetails userDetails = loadUserByUsername(userName);
		String newGeneratedToken = jwtUtil.generateToken(userDetails);

		Identity identity = identityRepository.findById(userName).get();
		JwtResponse jwtResponse = new JwtResponse(identity, newGeneratedToken);
		return ResponseEntity.ok(jwtResponse);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Identity identity = identityRepository.findById(username).get();

		if (identity != null) {
			return new org.springframework.security.core.userdetails.User(identity.getUsername(),
					identity.getPassword(), getAuthority(identity));
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}

	private Set<SimpleGrantedAuthority> getAuthority(Identity identity) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		identity.getRoleList().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
		});
		return authorities;
	}

	private void authenticate(String userName, String userPassword) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
