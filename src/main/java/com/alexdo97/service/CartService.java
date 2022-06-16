package com.alexdo97.service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.alexdo97.exception.AuthenticatedPrincialException;
import com.alexdo97.exception.HttpError;
import com.alexdo97.exception.ProductQuantityException;
import com.alexdo97.exception.TotalCartValueException;
import com.alexdo97.model.Cart;
import com.alexdo97.model.Customer;
import com.alexdo97.model.Product;
import com.alexdo97.model.ProductOrder;
import com.alexdo97.repository.CartRepository;
import com.alexdo97.repository.CustomerRepository;
import com.alexdo97.repository.ProductOrderRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CartService {

	private CustomerRepository customerRepository;
	private ProductOrderRepository productOrderRepository;
	private CartRepository cartRepository;
	private ProductService productService;

	public CartService() {

	}

	@Autowired
	public CartService(CustomerRepository customerRepository, ProductOrderRepository productOrderRepository,
			CartRepository cartRepository, ProductService productService) {
		this.customerRepository = customerRepository;
		this.productOrderRepository = productOrderRepository;
		this.cartRepository = cartRepository;
		this.productService = productService;
	}

	public ResponseEntity<Cart> getCart(User user) {

		Customer customer = null;
		Cart cart = null;

		try {
			customer = validateAndGetCustomer(user);
			cart = cartRepository.findById(customer.getCart().getId()).get();
		} catch (NoSuchElementException e) {
			log.error("Cart not found with id: " + customer.getCart().getId(), e);
			throw HttpError.badRequest("Cart not found");
		} catch (Exception e) {
			log.error("Unknown error in returning cart with id: " + cart.getId(), e);
			throw HttpError.internalServerError(HttpError.ERROR_MSG_UNKNOWN);
		}
		return ResponseEntity.ok(cart);
	}

	public ResponseEntity<Cart> addProductToCart(User user, Long productId, int quantity) {
		Cart cart = null;
		try {
			if (quantity == 0) {
				throw new ProductQuantityException("Quantity is 0");
			}
			cart = getCart(user).getBody();
			Product product = productService.getProductById(productId).getBody();
			ProductOrder existingProductOrder = productOrderRepository.findByCartIdAndProductId(cart.getId(),
					product.getId());
			if (existingProductOrder != null) {
				cart = updateExistingProductOrder(quantity, existingProductOrder, user);
			} else {
				ProductOrder newProductOrder = new ProductOrder(product, cart, quantity);
				productOrderRepository.save(newProductOrder);
				cart.calculateTotal();
				cart = cartRepository.save(cart);
			}
			log.info("Product with id: " + productId + " and quantity: " + quantity + " added to cart");
			return ResponseEntity.ok(cart);
		} catch (ProductQuantityException e) {
			log.error("Product order quantity attribute is 0", e);
			throw HttpError.badRequest("Quantity can't be 0");
		} catch (Exception e) {
			log.error("Unknown error in updating cart with id: " + cart.getId(), e);
			throw HttpError.internalServerError(HttpError.ERROR_MSG_UNKNOWN);
		}
	}

	public String sendOrder(User user) {
		Cart cart = null;
		Customer customer = null;
		try {
			cart = getCart(user).getBody();
			if (cart.getTotal() == 0) {
				throw new TotalCartValueException("Total of the cart is 0");
			}
			customer = validateAndGetCustomer(user);
			String orderConfirmationMessage = createMessage(customer);
			cart.getProductOrders().removeAll(cart.getProductOrders());
			cart.calculateTotal();
			cart = cartRepository.save(cart);
			log.info("Order sent!");
			return orderConfirmationMessage;
		} catch (TotalCartValueException e) {
			log.error("Total cart value is 0", e);
			throw HttpError.internalServerError("Can't send a order with empty cart");
		} catch (Exception e) {
			log.error("Unknown error in sendiong order: " + cart.getId(), e);
			throw HttpError.internalServerError(HttpError.ERROR_MSG_UNKNOWN);
		}
	}

	public void deleteProductOrder(User user, Long productOrderId) {
		Cart cart = null;
		try {
			cart = getCart(user).getBody();
			cart.getProductOrders().removeIf(po -> po.getId().equals(productOrderId));
			cart.calculateTotal();
			cart = cartRepository.save(cart);
		} catch (Exception e) {
			log.error("Unknown error in deleting product order with id: " + productOrderId, e);
			throw HttpError.internalServerError(HttpError.ERROR_MSG_UNKNOWN);
		}
	}

	// PRIVATE METHODS

	private Customer validateAndGetCustomer(User user) {
		try {
			Optional<Customer> customerOptional = customerRepository.findById(user.getUsername());
			if (customerOptional.isEmpty()) {
				throw new AuthenticatedPrincialException("User not found");
			}
			return customerOptional.get();
		} catch (AuthenticatedPrincialException e) {
			log.error("Currently logged user with username:" + user.getUsername() + " not found in database", e);
			throw HttpError.badRequest("Logged user does not exist anymore");
		}
	}

	private Cart updateExistingProductOrder(int quantity, ProductOrder existingProductOrder, User user) {
		Cart cart = null;
		try {
			existingProductOrder.setQuantity(existingProductOrder.getQuantity() + quantity);
			existingProductOrder.setRegisteredAt(LocalDateTime.now());
			productOrderRepository.save(existingProductOrder);
			cart = getCart(user).getBody();
			cart.calculateTotal();
			cart = cartRepository.save(cart);
			return cart;
		} catch (Exception e) {
			log.error("Unknown error in updating cart with id: " + cart.getId(), e);
			throw HttpError.internalServerError(HttpError.ERROR_MSG_UNKNOWN);
		}
	}

	private String createMessage(Customer customer) {
		StringBuilder sb = new StringBuilder();
		sb.append("Hello ");
		sb.append(customer.getFirstName());
		sb.append(" ");
		sb.append(customer.getLastName());
		sb.append("!\n");
		sb.append("We've received your store order with the total value of: ");
		sb.append(customer.getCart().getTotal());
		sb.append(" RON.\n");
		sb.append("Your products will arrive in front of your door as soon as possible!");
		return sb.toString();
	}

	// GETTERS AND SETTERS

	public CustomerRepository getCustomerRepository() {
		return customerRepository;
	}

	public void setCustomerRepository(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	public ProductOrderRepository getProductOrderRepository() {
		return productOrderRepository;
	}

	public void setProductOrderRepository(ProductOrderRepository productOrderRepository) {
		this.productOrderRepository = productOrderRepository;
	}

	public CartRepository getCartRepository() {
		return cartRepository;
	}

	public void setCartRepository(CartRepository cartRepository) {
		this.cartRepository = cartRepository;
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

}
