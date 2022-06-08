package com.alexdo97.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.alexdo97.model.Product;
import com.alexdo97.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	ProductRepository productRepository;

	public ResponseEntity<List<Product>> getProducts() {
		List<Product> productList = productRepository.findAll();
		return ResponseEntity.ok(productList);
	}

	public ResponseEntity<Product> getProductById(@PathVariable Long id) {
		Product product = productRepository.findById(id).get();
		return ResponseEntity.ok(product);
	}
}
