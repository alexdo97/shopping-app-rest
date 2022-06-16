package com.alexdo97.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.alexdo97.exception.HttpError;
import com.alexdo97.model.Identity;
import com.alexdo97.model.JwtRequest;
import com.alexdo97.model.JwtResponse;
import com.alexdo97.repository.IdentityRepository;
import com.alexdo97.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthService implements UserDetailsService {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private IdentityRepository identityRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private IdentityService identityService;

	public ResponseEntity<JwtResponse> createJwtToken(JwtRequest jwtRequest) {
		String userName = null;
		String userPassword = null;
		try {
			userName = jwtRequest.getUserName();
			userPassword = jwtRequest.getUserPassword();
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));

			UserDetails userDetails = loadUserByUsername(userName);
			String newGeneratedToken = jwtUtil.generateToken(userDetails);

			Identity identity = identityRepository.findById(userName).get();
			JwtResponse jwtResponse = new JwtResponse(identity, newGeneratedToken);
			return ResponseEntity.ok(jwtResponse);
		} catch (DisabledException e) {
			log.warn("User: " + userName + " is disabled", e);
			throw HttpError.badRequest("Can't login. User: " + userName + " is disabled");
		} catch (UsernameNotFoundException | BadCredentialsException | InternalAuthenticationServiceException e) {
			log.warn("Invalid credentials", e);
			throw HttpError.badRequest("Invalid username or password");
		} catch (Exception e) {
			log.error("Unknown error in login", e);
			throw HttpError.internalServerError(HttpError.ERROR_MSG_UNKNOWN);
		}

	}

	public ResponseEntity<Identity> createIdentity(Identity newIdentity) {
		return identityService.createIdentity(newIdentity);
	}

	public ResponseEntity<Identity> getIdentity(User user) {
		return identityService.getIdentity(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		Identity identity = identityRepository.findById(username).get();

		if (identity != null) {
			return new org.springframework.security.core.userdetails.User(identity.getUsername(),
					identity.getPassword(), getAuthority(identity));
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}

	// private methods

	private Set<SimpleGrantedAuthority> getAuthority(Identity identity) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		identity.getRoleList().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
		});
		return authorities;
	}
}
