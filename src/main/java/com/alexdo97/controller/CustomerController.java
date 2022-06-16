package com.alexdo97.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alexdo97.model.Customer;
import com.alexdo97.service.CustomerService;

@RestController
@RequestMapping(value = "/customer")
public class CustomerController {

	@Autowired
	CustomerService customerService;

	@PatchMapping("/firstName")
	public ResponseEntity<Customer> updateCustomerFirstName(@AuthenticationPrincipal User user,
			@RequestParam String firstName) {
		return customerService.updateCustomerFirstName(user, firstName);
	}

	@PatchMapping("/lastName")
	public ResponseEntity<Customer> updateCustomerLastName(@AuthenticationPrincipal User user,
			@RequestParam String lastName) {
		return customerService.updateCustomerLastName(user, lastName);
	}

	@PatchMapping("/email")
	public ResponseEntity<Customer> updateCustomerEmail(@AuthenticationPrincipal User user,
			@RequestParam String email) {
		return customerService.updateCustomerEmail(user, email);
	}

	@PatchMapping("/phoneNumber")
	public ResponseEntity<Customer> updateCustomerPhoneNumber(@AuthenticationPrincipal User user,
			@RequestParam String phoneNumber) {
		return customerService.updateCustomerPhoneNumber(user, phoneNumber);
	}

}
