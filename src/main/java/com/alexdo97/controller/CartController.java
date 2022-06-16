package com.alexdo97.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
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
	public ResponseEntity<Cart> getCart(@AuthenticationPrincipal User user) {
		return cartService.getCart(user);
	}

	@PostMapping("/{productId}/addProduct")
	public ResponseEntity<Cart> addProductToCart(@AuthenticationPrincipal User user, @PathVariable Long productId,
			@RequestParam int quantity) {
		return cartService.addProductToCart(user, productId, quantity);
	}

	@PostMapping("/sendOrder")
	public String sendOrder(@AuthenticationPrincipal User user) {
		return cartService.sendOrder(user);
	}

	@DeleteMapping("/{productOrderId}/deleteProductOrder")
	public void deleteProductOrder(@AuthenticationPrincipal User user, @PathVariable Long productOrderId) {
		cartService.deleteProductOrder(user, productOrderId);
	}

}
