package com.alexdo97.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.alexdo97.exception.EmailException;
import com.alexdo97.exception.HttpError;
import com.alexdo97.exception.PhoneNumberException;
import com.alexdo97.model.Customer;
import com.alexdo97.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerService {

	private CustomerRepository customerRepository;

	public CustomerService() {

	}

	@Autowired
	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	public ResponseEntity<Customer> updateCustomerFirstName(User user, String newFirstName) {
		try {
			Customer updatedCustomer = customerRepository.findById(user.getUsername()).get();
			updatedCustomer.setFirstName(newFirstName);
			Customer customer = customerRepository.save(updatedCustomer);
			log.info("Customer's first name with username: " + user.getUsername() + " updated");
			return ResponseEntity.ok(customer);
		} catch (NoSuchElementException e) {
			log.error("Customer not found with username: " + user.getUsername(), e);
			throw HttpError.notFound("Customer not found");
		} catch (Exception e) {
			log.error("Unknown error in updating customer with username: " + user.getUsername(), e);
			throw HttpError.internalServerError(HttpError.ERROR_MSG_UNKNOWN);
		}

	}

	public ResponseEntity<Customer> updateCustomerLastName(User user, String newLastName) {
		try {
			Customer updatedCustomer = customerRepository.findById(user.getUsername()).get();
			updatedCustomer.setLastName(newLastName);
			Customer customer = customerRepository.save(updatedCustomer);
			log.info("Customer's last name with username: " + user.getUsername() + " updated");
			return ResponseEntity.ok(customer);
		} catch (NoSuchElementException e) {
			log.error("Customer not found with username: " + user.getUsername(), e);
			throw HttpError.notFound("Customer not found");
		} catch (Exception e) {
			log.error("Unknown error in updating customer with username: " + user.getUsername(), e);
			throw HttpError.internalServerError(HttpError.ERROR_MSG_UNKNOWN);
		}
	}

	public ResponseEntity<Customer> updateCustomerEmail(User user, String newEmail) {
		Optional<Customer> existingCustomer = customerRepository.findByEmail(newEmail);
		try {
			if (existingCustomer.isPresent()) {
				throw new EmailException("Duplicate emails");
			}
			Customer updatedCustomer = customerRepository.findById(user.getUsername()).get();
			updatedCustomer.setEmail(newEmail);
			Customer customer = customerRepository.save(updatedCustomer);
			log.info("Customer's email with username: " + user.getUsername() + " updated");
			return ResponseEntity.ok(customer);
		} catch (EmailException e) {
			log.warn("Another customer already exists with email: " + newEmail, e);
			throw HttpError.badRequest("Customer already exists with email: " + newEmail);
		} catch (NoSuchElementException e) {
			log.error("Another customer not found with username: " + user.getUsername(), e);
			throw HttpError.notFound("Customer not found");
		} catch (Exception e) {
			log.error("Unknown error in updating customer with username: " + user.getUsername(), e);
			throw HttpError.internalServerError(HttpError.ERROR_MSG_UNKNOWN);
		}
	}

	public ResponseEntity<Customer> updateCustomerPhoneNumber(User user, String newPhoneNumber) {
		try {
			Optional<Customer> existingCustomer = customerRepository.findByPhoneNumber(newPhoneNumber);
			if (existingCustomer.isPresent()) {
				throw new PhoneNumberException("Duplicate phone numbers");
			}
			Customer updatedCustomer = customerRepository.findById(user.getUsername()).get();
			updatedCustomer.setPhoneNumber(newPhoneNumber);
			Customer customer = customerRepository.save(updatedCustomer);
			log.info("Customer's phone number with username: " + user.getUsername() + " updated");
			return ResponseEntity.ok(customer);
		} catch (PhoneNumberException e) {
			log.warn("Another customer already exists with phone number: " + newPhoneNumber, e);
			throw HttpError.badRequest("Another customer already exists with phone number: " + newPhoneNumber);
		} catch (NoSuchElementException e) {
			log.error("Customer not found with username: " + user.getUsername(), e);
			throw HttpError.notFound("Customer not found");
		} catch (Exception e) {
			log.error("Unknown error in updating customer with username: " + user.getUsername(), e);
			throw HttpError.internalServerError(HttpError.ERROR_MSG_UNKNOWN);
		}
	}

	// GETTERS AND SETTERS

	public CustomerRepository getCustomerRepository() {
		return customerRepository;
	}

	public void setCustomerRepository(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}
}
