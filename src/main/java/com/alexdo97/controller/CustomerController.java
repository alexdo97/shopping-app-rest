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
	public ResponseEntity<Customer> updateCustomer(@RequestBody Customer newCustomer, @PathVariable Long id) {
		return customerService.updateCustomer(newCustomer, id);
	}

	@PatchMapping("/{id}/firstName/{firstName}")
	public ResponseEntity<Customer> updateCustomerFirstName(@PathVariable Long id, @PathVariable String firstName) {
		return customerService.updateCustomerFirstName(id, firstName);
	}

	@PatchMapping("/{id}/lastName/{lastName}")
	public ResponseEntity<Customer> updateCustomerLastName(@PathVariable Long id, @PathVariable String lastName) {
		return customerService.updateCustomerLastName(id, lastName);
	}

	@PatchMapping("/{id}/email/{newEmail}")
	public ResponseEntity<Customer> updateCustomerEmail(@PathVariable Long id, @PathVariable String newEmail) {
		return customerService.updateCustomerEmail(id, newEmail);
	}

	@PatchMapping("/{id}/phoneNumber/{newPhoneNumber}")
	public ResponseEntity<Customer> updateCustomerPhoneNumber(@PathVariable Long id,
			@PathVariable String newPhoneNumber) {
		return customerService.updateCustomerPhoneNumber(id, newPhoneNumber);
	}

}
