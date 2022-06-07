package com.alexdo97.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alexdo97.enums.Category;
import com.alexdo97.model.Cart;
import com.alexdo97.model.Customer;
import com.alexdo97.model.Product;
import com.alexdo97.model.ProductOrder;
import com.alexdo97.repository.CartRepository;
import com.alexdo97.repository.CustomerRepository;
import com.alexdo97.repository.ProductOrderRepository;
import com.alexdo97.repository.ProductRepository;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	ProductOrderRepository productOrderRepository;

	@Autowired
	CartRepository cartRepository;

	Customer customer;
	Cart cart;

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

	@PostMapping("/{id}/add")
	public void addProductToCart(@PathVariable Long id, @RequestParam int quantity) {
		if (customer == null) {
			initCustomerAndCart();
		}

		Product product = productRepository.findById(id).get();
		ProductOrder existingProductOrder = productOrderRepository.findByCartIdAndProductId(cart.getId(),
				product.getId());

		if (existingProductOrder != null) {
			existingProductOrder.setQuantity(existingProductOrder.getQuantity() + quantity);
			existingProductOrder.setRegisteredAt(LocalDateTime.now());
			productOrderRepository.save(existingProductOrder);
			cart = getCurrentCart();
			cart.calculateTotal();
			cart = cartRepository.save(cart);
			System.out.println(cart.getProductOrders().toString());
			System.out.println(cart.getTotal());
			return;
		}

		ProductOrder newProductOrder = new ProductOrder(product, cart, quantity);
		productOrderRepository.save(newProductOrder);
		cart = getCurrentCart();
		cart.calculateTotal();
		cartRepository.save(cart);
		System.out.println(cart.getProductOrders().toString());
		System.out.println(cart.getTotal());

	}

	@PostMapping("/sendOrder")
	public String sendOrder() {
		cart = getCurrentCart();
		StringBuilder sb = new StringBuilder();
		sb.append("Hello ");
		sb.append(customer.getFirstName());
		sb.append(" ");
		sb.append(customer.getLastName());
		sb.append("!\n");
		sb.append("We've received your store order with the total value of: ");
		sb.append(cart.getTotal());
		sb.append(" RON.\n");
		sb.append("Your products will arrive in front of your door as soon as possible!");
		cart.getProductOrders().removeAll(cart.getProductOrders());
		cart.calculateTotal();
		cart = cartRepository.save(cart);
		return sb.toString();
	}

	@DeleteMapping("/{productOrderId}/deleteProductOrder")
	public void deleteProductOrder(@PathVariable Long productOrderId) {
		cart = getCurrentCart();
		cart.getProductOrders().removeIf(po -> po.getId().equals(productOrderId));
		cart.calculateTotal();
		cart = cartRepository.save(cart);
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

	private void initCustomerAndCart() {
		customer = customerRepository.findById(1L).get();
		cart = customer.getCart();
		System.out.println("Customer init");
	}

	private Cart getCurrentCart() {
		return cartRepository.findByCustomerId(customer.getId());
	}

}
