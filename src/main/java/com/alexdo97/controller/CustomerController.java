package com.alexdo97.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alexdo97.model.Customer;
import com.alexdo97.model.Product;
import com.alexdo97.repository.CustomerRepository;

@RestController
@RequestMapping(value = "/customer")
public class CustomerController {

	@Autowired
	CustomerRepository customerRepository;

	@GetMapping("")
	public List<Customer> getCustomers() {
		return customerRepository.findAll();
	}

	@GetMapping("/{id}")
	public Customer getCustomerById(@PathVariable Long id) {
		return customerRepository.findById(id).get();
	}

	@PostMapping("")
	public Customer createCustomer(@Valid @RequestBody Customer newCustomer) {
		return customerRepository.save(newCustomer);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Customer> updateProduct(@Valid @RequestBody Customer newCustomer, @PathVariable Long id) {
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

	@DeleteMapping("/{id}")
	public void deleteCustomer(@PathVariable Long id) {
		customerRepository.deleteById(id);
	}

	@PatchMapping("/{id}/{firstName}")
	public ResponseEntity<Customer> updateCustomerFirstName(@PathVariable Long id, @PathVariable String firstName) {
		Customer updatedCustomer = customerRepository.findById(id).get();
		updatedCustomer.setFirstName(firstName);
		customerRepository.save(updatedCustomer);
		return ResponseEntity.ok(updatedCustomer);
	}

	@PatchMapping("/{id}/{lastName}")
	public ResponseEntity<Customer> updateCustomerLastName(@PathVariable Long id, @PathVariable String lastName) {
		Customer updatedCustomer = customerRepository.findById(id).get();
		updatedCustomer.setFirstName(lastName);
		customerRepository.save(updatedCustomer);
		return ResponseEntity.ok(updatedCustomer);
	}

	@PatchMapping("/{id}/{newEmail}")
	public ResponseEntity<Customer> updateCustomerEmail(@PathVariable Long id, @PathVariable String newEmail) {
		Customer updatedCustomer = customerRepository.findById(id).get();
		updatedCustomer.setEmail(newEmail);
		customerRepository.save(updatedCustomer);
		return ResponseEntity.ok(updatedCustomer);
	}

	@PatchMapping("/{id}/{phoneNumber}")
	public ResponseEntity<Customer> updateCustomerPhoneNumber(@PathVariable Long id,
			@PathVariable String newPhoneNumber) {
		Customer updatedCustomer = customerRepository.findById(id).get();
		updatedCustomer.setPhoneNumber(newPhoneNumber);
		customerRepository.save(updatedCustomer);
		return ResponseEntity.ok(updatedCustomer);
	}

}
