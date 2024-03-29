package com.alexdo97.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alexdo97.exception.EmailException;
import com.alexdo97.exception.HttpError;
import com.alexdo97.exception.PhoneNumberException;
import com.alexdo97.exception.UsernameException;
import com.alexdo97.model.Cart;
import com.alexdo97.model.Customer;
import com.alexdo97.model.Identity;
import com.alexdo97.model.Role;
import com.alexdo97.repository.CustomerRepository;
import com.alexdo97.repository.IdentityRepository;
import com.alexdo97.repository.RoleRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class IdentityService {

	private IdentityRepository identityRepository;
	private RoleRepository roleRepository;
	private CustomerRepository customerRepository;
	private PasswordEncoder passwordEncoder;

	public IdentityService() {

	}

	@Autowired
	public IdentityService(IdentityRepository identityRepository, RoleRepository roleRepository,
			CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
		this.identityRepository = identityRepository;
		this.roleRepository = roleRepository;
		this.customerRepository = customerRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public ResponseEntity<Identity> createIdentity(Identity newIdentity) {
		Customer customerDetails = null;
		try {
			customerDetails = newIdentity.getCustomer();
			validateCustomerDetails(newIdentity, customerDetails);
			prepareNewIdentity(newIdentity);
			Identity savedIdentity = identityRepository.save(newIdentity);
			savedIdentity.setPassword("REDACTED");
			log.info("New identity created");
			return ResponseEntity.ok(savedIdentity);
		} catch (UsernameException e) {
			log.warn("Identity already exists with username: " + newIdentity.getUsername(), e);
			throw HttpError.badRequest("Identity already exists with username: " + newIdentity.getUsername());
		} catch (EmailException e) {
			log.warn("Identity already exists with email: " + customerDetails.getEmail(), e);
			throw HttpError.badRequest("Identity already exists with email: " + customerDetails.getEmail());
		} catch (PhoneNumberException e) {
			log.warn("Identity already exists with phone number: " + customerDetails.getPhoneNumber(), e);
			throw HttpError
					.badRequest("Identity already exists with phone number: " + customerDetails.getPhoneNumber());
		} catch (NoSuchElementException e) {
			log.error("One of the roles provided was not found in database", e);
			throw HttpError.badRequest("Provided role(s) does not exist");
		} catch (Exception e) {
			log.error("Unknown error in register new identity", e);
			throw HttpError.internalServerError(HttpError.ERROR_MSG_UNKNOWN);
		}

	}

	public ResponseEntity<Identity> getIdentity(User user) {
		try {
			Identity identity = identityRepository.findById(user.getUsername()).get();
			identity.setPassword("REDACTED");
			return ResponseEntity.ok(identity);
		} catch (Exception e) {
			log.error("Unknown error in returning identity with username: " + user.getUsername(), e);
			throw HttpError.internalServerError(HttpError.ERROR_MSG_UNKNOWN);
		}
	}

	public String getEncodedPassword(String password) {
		return passwordEncoder.encode(password);
	}

	// PRIVATE METHODS

	private void validateCustomerDetails(Identity newIdentity, Customer customerDetails)
			throws UsernameException, EmailException, PhoneNumberException {
		Optional<Customer> customerOptional;
		customerOptional = customerRepository.findById(newIdentity.getUsername());
		if (!customerOptional.isEmpty()) {
			throw new UsernameException("Duplicate identities");
		}
		customerOptional = customerRepository.findByEmail(customerDetails.getEmail());
		if (!customerOptional.isEmpty()) {
			throw new EmailException("Duplicate emails");
		}
		customerOptional = customerRepository.findByPhoneNumber(customerDetails.getPhoneNumber());
		if (!customerOptional.isEmpty()) {
			throw new PhoneNumberException("Duplicate phone numbers");
		}
	}

	private void prepareNewIdentity(Identity newIdentity) throws NoSuchElementException {
		List<Role> newRoleList = new ArrayList<>();
		newIdentity.getRoleList().forEach(role -> newRoleList.add(roleRepository.findById(role.getRoleName()).get()));
		newIdentity.getCustomer().setUsername(newIdentity.getUsername());
		newIdentity.setPassword(getEncodedPassword(newIdentity.getPassword()));
		newIdentity.setRoleList(newRoleList);
		newIdentity.getCustomer().setCart(new Cart());
	}

	// GETTERS AND SETTERS

	public IdentityRepository getIdentityRepository() {
		return identityRepository;
	}

	public void setIdentityRepository(IdentityRepository identityRepository) {
		this.identityRepository = identityRepository;
	}

	public RoleRepository getRoleRepository() {
		return roleRepository;
	}

	public void setRoleRepository(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public CustomerRepository getCustomerRepository() {
		return customerRepository;
	}

	public void setCustomerRepository(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

}
