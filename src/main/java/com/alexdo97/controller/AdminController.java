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
import com.alexdo97.service.AdminService;

@RestController
@RequestMapping(value = "/admin")
public class AdminController {

	@Autowired
	AdminService adminService;

	// Customer end-points

	@GetMapping("/customer")
	public ResponseEntity<List<Customer>> getCustomers() {
		return adminService.getCustomers();
	}

	@GetMapping("/customer/{id}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
		return adminService.getCustomerById(id);
	}

	@DeleteMapping("/customer/{id}")
	public void deleteCustomer(@PathVariable Long id) {
		adminService.deleteCustomer(id);
	}

	// Product end-points

	@PostMapping("/product")
	public ResponseEntity<Product> createProduct(@RequestBody Product newProduct) {
		return adminService.createProduct(newProduct);
	}

	@PutMapping("/product/{id}")
	public ResponseEntity<Product> updateProduct(@RequestBody Product newProduct, @PathVariable Long id) {
		return adminService.updateProduct(newProduct, id);
	}

	@DeleteMapping("/product/{id}")
	public void deleteProduct(@PathVariable Long id) {
		adminService.deleteProduct(id);
	}

	@PatchMapping("/product/{id}/name/{newName}")
	public ResponseEntity<Product> updateProductName(@PathVariable Long id, @PathVariable String newName) {
		return adminService.updateProductName(id, newName);
	}

	@PatchMapping("/product/{id}/category/{newCategory}")
	public ResponseEntity<Product> updateProductCategory(@PathVariable Long id, @PathVariable Category newCategory) {
		return adminService.updateProductCategory(id, newCategory);
	}

	@PatchMapping("/product/{id}/price/{newPrice}")
	public ResponseEntity<Product> updateProductPrice(@PathVariable Long id, @PathVariable double newPrice) {
		return adminService.updateProductPrice(id, newPrice);
	}

	// Cart end-points

	@GetMapping("/cart")
	public ResponseEntity<List<Cart>> getCarts() {
		return adminService.getCarts();
	}

	@GetMapping("/cart/{id}")
	public ResponseEntity<Cart> getCartById(@PathVariable Long id) {
		return adminService.getCartById(id);
	}
}
