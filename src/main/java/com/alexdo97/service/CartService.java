package com.alexdo97.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.alexdo97.model.Cart;
import com.alexdo97.model.Customer;
import com.alexdo97.model.Product;
import com.alexdo97.model.ProductOrder;
import com.alexdo97.repository.CartRepository;
import com.alexdo97.repository.CustomerRepository;
import com.alexdo97.repository.ProductOrderRepository;
import com.alexdo97.repository.ProductRepository;

@Service
public class CartService {

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

	public void addProductToCart(@PathVariable Long id, @RequestParam int quantity) {
		if (customer == null) {
			initCustomerAndCart();
		}

		Product product = productRepository.findById(id).get();
		ProductOrder existingProductOrder = productOrderRepository.findByCartIdAndProductId(cart.getId(),
				product.getId());

		if (existingProductOrder != null) {
			updateExistingProductOrder(quantity, existingProductOrder);
		}

		ProductOrder newProductOrder = new ProductOrder(product, cart, quantity);
		productOrderRepository.save(newProductOrder);
		cart = getCurrentCart();
		cart.calculateTotal();
		cartRepository.save(cart);
		System.out.println(cart.getProductOrders().toString());
		System.out.println(cart.getTotal());
	}

	public String sendOrder() {
		cart = getCurrentCart();
		String orderConfirmationMessage = createMessage();
		cart.getProductOrders().removeAll(cart.getProductOrders());
		cart.calculateTotal();
		cart = cartRepository.save(cart);
		return orderConfirmationMessage;
	}

	public void deleteProductOrder(@PathVariable Long productOrderId) {
		cart = getCurrentCart();
		cart.getProductOrders().removeIf(po -> po.getId().equals(productOrderId));
		cart.calculateTotal();
		cart = cartRepository.save(cart);
	}

	// Until methods

	private void initCustomerAndCart() {
		customer = customerRepository.findById(1L).get();
		cart = customer.getCart();
		System.out.println("Customer init");
	}

	private Cart getCurrentCart() {
		return cartRepository.findByCustomerId(customer.getId());
	}

	private void updateExistingProductOrder(int quantity, ProductOrder existingProductOrder) {
		existingProductOrder.setQuantity(existingProductOrder.getQuantity() + quantity);
		existingProductOrder.setRegisteredAt(LocalDateTime.now());
		productOrderRepository.save(existingProductOrder);
		cart = getCurrentCart();
		cart.calculateTotal();
		cart = cartRepository.save(cart);
		System.out.println(cart.getProductOrders().toString());
		System.out.println(cart.getTotal());
	}

	private String createMessage() {
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
		return sb.toString();
	}

}
