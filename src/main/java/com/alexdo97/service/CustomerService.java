package com.alexdo97.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.alexdo97.model.Customer;
import com.alexdo97.repository.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	CustomerRepository customerRepository;

	public ResponseEntity<Customer> createCustomer(@RequestBody Customer newCustomer) {
		Customer savedCustomer = customerRepository.save(newCustomer);
		return ResponseEntity.ok(savedCustomer);
	}

	public ResponseEntity<Customer> updateCustomer(@RequestBody Customer newCustomer, @PathVariable String username) {
		Customer updatedCustomer = customerRepository.findById(username).map(customer -> {
			customer.setFirstName(newCustomer.getFirstName());
			customer.setLastName(newCustomer.getLastName());
			customer.setEmail(newCustomer.getEmail());
			customer.setPhoneNumber(newCustomer.getPhoneNumber());
			return customerRepository.save(customer);
		}).orElseGet(() -> {
			newCustomer.setUsername(username);
			return customerRepository.save(newCustomer);
		});

		return ResponseEntity.ok(updatedCustomer);
	}

	public ResponseEntity<Customer> updateCustomerFirstName(@PathVariable String username,
			@PathVariable String firstName) {
		Customer updatedCustomer = customerRepository.findById(username).get();
		updatedCustomer.setFirstName(firstName);
		Customer customer = customerRepository.save(updatedCustomer);
		return ResponseEntity.ok(customer);
	}

	public ResponseEntity<Customer> updateCustomerLastName(@PathVariable String username,
			@PathVariable String lastName) {
		Customer updatedCustomer = customerRepository.findById(username).get();
		updatedCustomer.setLastName(lastName);
		Customer customer = customerRepository.save(updatedCustomer);
		return ResponseEntity.ok(customer);
	}

	public ResponseEntity<Customer> updateCustomerEmail(@PathVariable String username, @PathVariable String newEmail) {
		Customer updatedCustomer = customerRepository.findById(username).get();
		updatedCustomer.setEmail(newEmail);
		Customer customer = customerRepository.save(updatedCustomer);
		return ResponseEntity.ok(customer);
	}

	public ResponseEntity<Customer> updateCustomerPhoneNumber(@PathVariable String username,
			@PathVariable String newPhoneNumber) {
		Customer updatedCustomer = customerRepository.findById(username).get();
		updatedCustomer.setPhoneNumber(newPhoneNumber);
		Customer customer = customerRepository.save(updatedCustomer);
		return ResponseEntity.ok(customer);
	}
}
