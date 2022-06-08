package com.alexdo97.controller;

import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alexdo97.model.Cart;
import com.alexdo97.model.Customer;
import com.alexdo97.model.Product;
import com.alexdo97.model.ProductOrder;
import com.alexdo97.repository.CartRepository;
import com.alexdo97.repository.CustomerRepository;
import com.alexdo97.repository.ProductOrderRepository;
import com.alexdo97.repository.ProductRepository;

@RestController
@RequestMapping(value = "/cart")
public class CartController {

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	ProductOrderRepository productOrderRepository;

	@Autowired
	CartRepository cartRepository;

	Customer customer;
	Cart cart;

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

	private void initCustomerAndCart() {
		customer = customerRepository.findById(1L).get();
		cart = customer.getCart();
		System.out.println("Customer init");
	}

	private Cart getCurrentCart() {
		return cartRepository.findByCustomerId(customer.getId());
	}

}
