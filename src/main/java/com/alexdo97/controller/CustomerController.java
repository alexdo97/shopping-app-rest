package com.alexdo97.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alexdo97.model.Customer;
import com.alexdo97.service.CustomerService;

@RestController
@RequestMapping(value = "/customer")
public class CustomerController {

	@Autowired
	CustomerService customerService;

	@PostMapping()
	public ResponseEntity<Customer> createCustomer(@RequestBody Customer newCustomer) {
		return customerService.createCustomer(newCustomer);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Customer> updateCustomer(@RequestBody Customer newCustomer, @PathVariable String username) {
		return customerService.updateCustomer(newCustomer, username);
	}

	@PatchMapping("/{id}/firstName/{firstName}")
	public ResponseEntity<Customer> updateCustomerFirstName(@PathVariable String username,
			@PathVariable String firstName) {
		return customerService.updateCustomerFirstName(username, firstName);
	}

	@PatchMapping("/{id}/lastName/{lastName}")
	public ResponseEntity<Customer> updateCustomerLastName(@PathVariable String username,
			@PathVariable String lastName) {
		return customerService.updateCustomerLastName(username, lastName);
	}

	@PatchMapping("/{id}/email/{newEmail}")
	public ResponseEntity<Customer> updateCustomerEmail(@PathVariable String username, @PathVariable String newEmail) {
		return customerService.updateCustomerEmail(username, newEmail);
	}

	@PatchMapping("/{id}/phoneNumber/{newPhoneNumber}")
	public ResponseEntity<Customer> updateCustomerPhoneNumber(@PathVariable String username,
			@PathVariable String newPhoneNumber) {
		return customerService.updateCustomerPhoneNumber(username, newPhoneNumber);
	}

}
