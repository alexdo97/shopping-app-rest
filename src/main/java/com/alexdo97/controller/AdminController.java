package com.alexdo97.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alexdo97.enums.Category;
import com.alexdo97.model.Cart;
import com.alexdo97.model.Customer;
import com.alexdo97.model.Product;
import com.alexdo97.model.Role;
import com.alexdo97.service.AdminService;

@RestController
@RequestMapping(value = "/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	@Autowired
	private AdminService adminService;

	// Customer end-points

	@GetMapping("/customer")
	public ResponseEntity<List<Customer>> getCustomers() {
		return adminService.getCustomers();
	}

	@GetMapping("/customer/{username}")
	public ResponseEntity<Customer> getCustomerByUsername(@PathVariable String username) {
		return adminService.getCustomerByUsername(username);
	}

	@DeleteMapping("/customer/{username}")
	public void deleteCustomer(@AuthenticationPrincipal User user, @PathVariable String username) {
		adminService.deleteCustomer(user, username);
	}

	// Product end-points

	@PostMapping("/product")
	public ResponseEntity<Product> createProduct(@RequestBody Product newProduct) {
		return adminService.createProduct(newProduct);
	}

	@PutMapping("/product/{id}")
	public ResponseEntity<Product> updateProduct(@RequestBody Product productDetails, @PathVariable Long id) {
		return adminService.updateProduct(productDetails, id);
	}

	@DeleteMapping("/product/{id}")
	public void deleteProduct(@PathVariable Long id) {
		adminService.deleteProduct(id);
	}

	@PatchMapping("/product/{id}/name")
	public ResponseEntity<Product> updateProductName(@PathVariable Long id, @RequestParam String name) {
		return adminService.updateProductName(id, name);
	}

	@PatchMapping("/product/{id}/category")
	public ResponseEntity<Product> updateProductCategory(@PathVariable Long id, @RequestParam Category category) {
		return adminService.updateProductCategory(id, category);
	}

	@PatchMapping("/product/{id}/price")
	public ResponseEntity<Product> updateProductPrice(@PathVariable Long id, @RequestParam double price) {
		return adminService.updateProductPrice(id, price);
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

	// Role end-points

	@GetMapping("/role")
	public ResponseEntity<List<Role>> getRoles() {
		return adminService.getRoles();
	}

	@PostMapping("/role")
	public ResponseEntity<Role> createRole(@RequestBody Role newRole) {
		return adminService.createRole(newRole);
	}
}
