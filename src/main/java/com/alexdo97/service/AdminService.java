package com.alexdo97.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.alexdo97.enums.Category;
import com.alexdo97.exception.HttpError;
import com.alexdo97.model.Cart;
import com.alexdo97.model.Customer;
import com.alexdo97.model.Product;
import com.alexdo97.model.Role;
import com.alexdo97.repository.CartRepository;
import com.alexdo97.repository.CustomerRepository;
import com.alexdo97.repository.ProductRepository;
import com.alexdo97.repository.RoleRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminService {

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	RoleRepository roleRepository;

	public ResponseEntity<List<Customer>> getCustomers() {
		List<Customer> customerList = customerRepository.findAll();
		return ResponseEntity.ok(customerList);
	}

	public ResponseEntity<Customer> getCustomerById(@PathVariable String username) {
		try {
			Customer customer = customerRepository.findById(username).get();
			return ResponseEntity.ok(customer);
		} catch (NoSuchElementException e) {
			log.error("Customer not found with username: " + username, e);
			throw HttpError.notFound("Customer not found");
		}
	}

	public void deleteCustomer(@PathVariable String username) {
		try {
			customerRepository.deleteById(username);
		} catch (EmptyResultDataAccessException e) {
			log.error("Couldn't delete customer. Customer not found with username: " + username, e);
			throw HttpError.notFound("Customer not found");
		}
	}

	public ResponseEntity<Product> createProduct(@RequestBody Product newProduct) {
		try {
			Product product = productRepository.save(newProduct);
			return ResponseEntity.ok(product);
		} catch (EmptyResultDataAccessException e) {
			log.error("Null product object", e);
			throw HttpError.badRequest("Missing product data");
		}
	}

	public ResponseEntity<Product> updateProduct(@RequestBody Product newProduct, @PathVariable Long id) {
		try {
			Product updatedProduct = productRepository.findById(id).map(product -> {
				product.setName(newProduct.getName());
				product.setCategory(newProduct.getCategory());
				product.setPrice(newProduct.getPrice());
				return productRepository.save(product);
			}).orElseGet(() -> {
				newProduct.setId(id);
				return productRepository.save(newProduct);
			});

			return ResponseEntity.ok(updatedProduct);
		} catch (DataIntegrityViolationException e) {
			log.error("Hibernate error. Violated columns nullable constraints", e);
			throw HttpError.badRequest("The mandatory required attributes were not provided");
		}

	}

	public void deleteProduct(@PathVariable Long id) {
		try {
			productRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			log.error("Couldn't delete product. Product not found with id: " + id, e);
			throw HttpError.notFound("Couldn't delete product. Product not found with id: " + id);
		}
	}

	public ResponseEntity<Product> updateProductName(@PathVariable Long id, @PathVariable String newName) {
		Product updatedProduct = productRepository.findById(id).get();
		updatedProduct.setName(newName);
		productRepository.save(updatedProduct);
		return ResponseEntity.ok(updatedProduct);
	}

	public ResponseEntity<Product> updateProductCategory(@PathVariable Long id, @PathVariable Category newCategory) {
		Product updatedProduct = productRepository.findById(id).get();
		updatedProduct.setCategory(newCategory);
		productRepository.save(updatedProduct);
		return ResponseEntity.ok(updatedProduct);
	}

	public ResponseEntity<Product> updateProductPrice(@PathVariable Long id, @PathVariable double newPrice) {
		Product updatedProduct = productRepository.findById(id).get();
		updatedProduct.setPrice(newPrice);
		productRepository.save(updatedProduct);
		return ResponseEntity.ok(updatedProduct);
	}

	public ResponseEntity<List<Cart>> getCarts() {
		List<Cart> cartList = cartRepository.findAll();
		return ResponseEntity.ok(cartList);
	}

	public ResponseEntity<Cart> getCartById(@PathVariable Long id) {
		Cart cart = cartRepository.findById(id).get();
		return ResponseEntity.ok(cart);
	}

	public ResponseEntity<Role> createRole(@RequestBody Role newRole) {
		Role role = roleRepository.save(newRole);
		return ResponseEntity.ok(role);
	}
}
