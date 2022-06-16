package com.alexdo97.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alexdo97.exception.HttpError;
import com.alexdo97.model.Product;
import com.alexdo97.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductService {

	@Autowired
	ProductRepository productRepository;

	public ResponseEntity<List<Product>> getProducts() {
		try {
			List<Product> productList = productRepository.findAll();
			return ResponseEntity.ok(productList);
		} catch (Exception e) {
			log.error("Unknown error in returning products", e);
			throw HttpError.internalServerError(HttpError.ERROR_MSG_UNKNOWN);
		}
	}

	public ResponseEntity<Product> getProductById(Long id) {
		try {
			Product product = productRepository.findById(id).get();
			return ResponseEntity.ok(product);
		} catch (NoSuchElementException e) {
			log.error("Product not found with id: " + id, e);
			throw HttpError.notFound("Product not found with id: " + id);
		} catch (Exception e) {
			log.error("Unknown error in returning product with id: " + id, e);
			throw HttpError.internalServerError(HttpError.ERROR_MSG_UNKNOWN);
		}
	}
}
