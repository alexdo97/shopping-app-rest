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

	public ResponseEntity<Customer> updateCustomer(@RequestBody Customer newCustomer, @PathVariable Long id) {
		Customer updatedCustomer = customerRepository.findById(id).map(customer -> {
			customer.setFirstName(newCustomer.getFirstName());
			customer.setLastName(newCustomer.getLastName());
			customer.setEmail(newCustomer.getEmail());
			customer.setPhoneNumber(newCustomer.getPhoneNumber());
			return customerRepository.save(customer);
		}).orElseGet(() -> {
			newCustomer.setId(id);
			return customerRepository.save(newCustomer);
		});

		return ResponseEntity.ok(updatedCustomer);
	}

	public ResponseEntity<Customer> updateCustomerFirstName(@PathVariable Long id, @PathVariable String firstName) {
		Customer updatedCustomer = customerRepository.findById(id).get();
		updatedCustomer.setFirstName(firstName);
		customerRepository.save(updatedCustomer);
		return ResponseEntity.ok(updatedCustomer);
	}

	public ResponseEntity<Customer> updateCustomerLastName(@PathVariable Long id, @PathVariable String lastName) {
		Customer updatedCustomer = customerRepository.findById(id).get();
		updatedCustomer.setLastName(lastName);
		customerRepository.save(updatedCustomer);
		return ResponseEntity.ok(updatedCustomer);
	}

	public ResponseEntity<Customer> updateCustomerEmail(@PathVariable Long id, @PathVariable String newEmail) {
		Customer updatedCustomer = customerRepository.findById(id).get();
		updatedCustomer.setEmail(newEmail);
		customerRepository.save(updatedCustomer);
		return ResponseEntity.ok(updatedCustomer);
	}

	public ResponseEntity<Customer> updateCustomerPhoneNumber(@PathVariable Long id,
			@PathVariable String newPhoneNumber) {
		Customer updatedCustomer = customerRepository.findById(id).get();
		updatedCustomer.setPhoneNumber(newPhoneNumber);
		customerRepository.save(updatedCustomer);
		return ResponseEntity.ok(updatedCustomer);
	}
}
