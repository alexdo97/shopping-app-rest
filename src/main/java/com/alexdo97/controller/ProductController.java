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

import com.alexdo97.enums.Category;
import com.alexdo97.model.Product;
import com.alexdo97.repository.ProductRepository;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

	@Autowired
	ProductRepository productRepository;

	@GetMapping("")
	public List<Product> getProducts() {
		return productRepository.findAll();
	}

	@GetMapping("/{id}")
	public Product getProductById(@PathVariable Long id) {
		return productRepository.findById(id).get();
	}

	@PostMapping("")
	public Product createProduct(@Valid @RequestBody Product newProduct) {
		return productRepository.save(newProduct);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Product> updateProduct(@Valid @RequestBody Product newProduct, @PathVariable Long id) {
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

	@DeleteMapping("/{id}")
	public void deleteProduct(@PathVariable Long id) {
		productRepository.deleteById(id);
	}

	@PatchMapping("/{id}/name/{newName}")
	public ResponseEntity<Product> updateProductName(@PathVariable Long id, @PathVariable String newName) {
		Product updatedProduct = productRepository.findById(id).get();
		updatedProduct.setName(newName);
		productRepository.save(updatedProduct);
		return ResponseEntity.ok(updatedProduct);
	}

	@PatchMapping("/{id}/category/{newCategory}")
	public ResponseEntity<Product> updateProductCategory(@PathVariable Long id, @PathVariable Category newCategory) {
		Product updatedProduct = productRepository.findById(id).get();
		updatedProduct.setCategory(newCategory);
		productRepository.save(updatedProduct);
		return ResponseEntity.ok(updatedProduct);
	}

	@PatchMapping("/{id}/price/{newPrice}")
	public ResponseEntity<Product> updateProductPrice(@PathVariable Long id, @PathVariable double newPrice) {
		Product updatedProduct = productRepository.findById(id).get();
		updatedProduct.setPrice(newPrice);
		productRepository.save(updatedProduct);
		return ResponseEntity.ok(updatedProduct);
	}

}
