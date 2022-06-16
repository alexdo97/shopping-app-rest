package com.alexdo97;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.alexdo97.enums.Category;
import com.alexdo97.exception.HttpError;
import com.alexdo97.model.Product;
import com.alexdo97.service.AdminService;
import com.alexdo97.service.ProductService;

@SpringBootTest
class ApplicationTests {

	@Autowired
	ProductService productService;

	@Autowired
	AdminService adminService;

	@Test
	public void createProductTest() {
		Product product = new Product("Brush", Category.Cosmetics, 10);
		ResponseEntity<Product> productResponse = adminService.createProduct(product);
		Assertions.assertNotNull(productResponse.getBody());
		Product productBody = productResponse.getBody();
		Assertions.assertNotNull(productBody.getId());
		Assertions.assertEquals(productBody.getName(), product.getName());
	}

	@Test
	public void deleteProductTest() {
		Product product = new Product("Brush", Category.Cosmetics, 10);
		ResponseEntity<Product> productResponse = adminService.createProduct(product);
		Assertions.assertNotNull(productResponse.getBody());
		Product productBody = productResponse.getBody();
		Assertions.assertNotNull(productBody.getId());
		Assertions.assertEquals(productBody.getName(), product.getName());
		adminService.deleteProduct(productBody.getId());
		HttpError httpError = Assertions.assertThrows(HttpError.class,
				() -> productService.getProductById(productBody.getId()).getBody());
		Assertions.assertEquals(HttpStatus.NOT_FOUND, httpError.getStatus());
	}

}
