package com.alexdo97.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.alexdo97.enums.Category;
import com.alexdo97.model.Cart;
import com.alexdo97.model.Customer;
import com.alexdo97.model.Product;
import com.alexdo97.repository.CartRepository;
import com.alexdo97.repository.CustomerRepository;
import com.alexdo97.repository.ProductRepository;

@Service
public class AdminService {

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CartRepository cartRepository;

	public ResponseEntity<List<Customer>> getCustomers() {
		List<Customer> customerList = customerRepository.findAll();
		return ResponseEntity.ok(customerList);
	}

	public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
		Customer customer = customerRepository.findById(id).get();
		return ResponseEntity.ok(customer);
	}

	public void deleteCustomer(@PathVariable Long id) {
		customerRepository.deleteById(id);
	}

	public ResponseEntity<Product> createProduct(@RequestBody Product newProduct) {
		Product product = productRepository.save(newProduct);
		return ResponseEntity.ok(product);
	}

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

	public void deleteProduct(@PathVariable Long id) {
		productRepository.deleteById(id);
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
}
