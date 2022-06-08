package com.alexdo97.controller;

import java.util.List;

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

import com.alexdo97.enums.Category;
import com.alexdo97.model.Cart;
import com.alexdo97.model.Customer;
import com.alexdo97.model.Product;
import com.alexdo97.repository.CartRepository;
import com.alexdo97.repository.CustomerRepository;
import com.alexdo97.repository.ProductRepository;

@RestController
@RequestMapping(value = "/admin")
public class AdminController {

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CartRepository cartRepository;

	// Customer end-points

	@GetMapping("/customer")
	public ResponseEntity<List<Customer>> getCustomers() {
		List<Customer> customerList = customerRepository.findAll();
		return ResponseEntity.ok(customerList);
	}

	@GetMapping("/customer/{id}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
		Customer customer = customerRepository.findById(id).get();
		return ResponseEntity.ok(customer);
	}

	@DeleteMapping("/customer/{id}")
	public void deleteCustomer(@PathVariable Long id) {
		customerRepository.deleteById(id);
	}

	// Product end-points

	@PostMapping("/product")
	public ResponseEntity<Product> createProduct(@RequestBody Product newProduct) {
		Product product = productRepository.save(newProduct);
		return ResponseEntity.ok(product);
	}

	@PutMapping("/product/{id}")
	public ResponseEntity<Product> updateProduct(@RequestBody Product newProduct, @PathVariable Long id) {
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
	}

	@DeleteMapping("/product/{id}")
	public void deleteProduct(@PathVariable Long id) {
		productRepository.deleteById(id);
	}

	@PatchMapping("/product/{id}/name/{newName}")
	public ResponseEntity<Product> updateProductName(@PathVariable Long id, @PathVariable String newName) {
		Product updatedProduct = productRepository.findById(id).get();
		updatedProduct.setName(newName);
		productRepository.save(updatedProduct);
		return ResponseEntity.ok(updatedProduct);
	}

	@PatchMapping("/product/{id}/category/{newCategory}")
	public ResponseEntity<Product> updateProductCategory(@PathVariable Long id, @PathVariable Category newCategory) {
		Product updatedProduct = productRepository.findById(id).get();
		updatedProduct.setCategory(newCategory);
		productRepository.save(updatedProduct);
		return ResponseEntity.ok(updatedProduct);
	}

	@PatchMapping("/product/{id}/price/{newPrice}")
	public ResponseEntity<Product> updateProductPrice(@PathVariable Long id, @PathVariable double newPrice) {
		Product updatedProduct = productRepository.findById(id).get();
		updatedProduct.setPrice(newPrice);
		productRepository.save(updatedProduct);
		return ResponseEntity.ok(updatedProduct);
	}

	// Cart end-points

	@GetMapping("/cart")
	public ResponseEntity<List<Cart>> getCarts() {
		List<Cart> cartList = cartRepository.findAll();
		return ResponseEntity.ok(cartList);
	}

	@GetMapping("/cart/{id}")
	public ResponseEntity<Cart> getCartById(@PathVariable Long id) {
		Cart cart = cartRepository.findById(id).get();
		return ResponseEntity.ok(cart);
	}
}
