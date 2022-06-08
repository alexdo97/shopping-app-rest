package com.alexdo97.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alexdo97.model.Cart;
import com.alexdo97.service.CartService;

@RestController
@RequestMapping(value = "/cart")
public class CartController {

	@Autowired
	CartService cartService;

	@GetMapping()
	public ResponseEntity<Cart> getCart() {
		return cartService.getCart();
	}

	@PostMapping("/{id}/add")
	public void addProductToCart(@PathVariable Long id, @RequestParam int quantity) {
		cartService.addProductToCart(id, quantity);
	}

	@PostMapping("/sendOrder")
	public String sendOrder() {
		return cartService.sendOrder();
	}

	@DeleteMapping("/{productOrderId}/deleteProductOrder")
	public void deleteProductOrder(@PathVariable Long productOrderId) {
		cartService.deleteProductOrder(productOrderId);
	}

}
