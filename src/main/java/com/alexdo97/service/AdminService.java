package com.alexdo97.service;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.alexdo97.enums.Category;
import com.alexdo97.exception.AuthenticatedPrincialException;
import com.alexdo97.exception.HttpError;
import com.alexdo97.exception.NullAttributeException;
import com.alexdo97.model.Cart;
import com.alexdo97.model.Customer;
import com.alexdo97.model.Product;
import com.alexdo97.model.Role;
import com.alexdo97.repository.CartRepository;
import com.alexdo97.repository.CustomerRepository;
import com.alexdo97.repository.ProductRepository;
import com.alexdo97.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

	private final CustomerRepository customerRepository;
	private final ProductRepository productRepository;
	private final CartRepository cartRepository;
	private final RoleRepository roleRepository;

	public ResponseEntity<List<Customer>> getCustomers() {
		try {
			List<Customer> customerList = customerRepository.findAll();
			return ResponseEntity.ok(customerList);
		} catch (Exception e) {
			log.error("Unknown error in returning customers", e);
			throw HttpError.internalServerError(HttpError.ERROR_MSG_UNKNOWN);
		}
	}

	public ResponseEntity<Customer> getCustomerByUsername(@PathVariable String username) {
		try {
			Customer customer = customerRepository.findById(username).get();
			return ResponseEntity.ok(customer);
		} catch (NoSuchElementException e) {
			log.error("Customer not found with username: " + username, e);
			throw HttpError.notFound("Customer not found with username: " + username);
		} catch (Exception e) {
			log.error("Unknown error in returning customer with username: " + username, e);
			throw HttpError.internalServerError(HttpError.ERROR_MSG_UNKNOWN);
		}
	}

	public void deleteCustomer(User user, String username) {
		try {
			if (user.getUsername().equals(username)) {
				throw new AuthenticatedPrincialException(
						"Authenticated principal identity conflicts with provided identity for deletion");
			}
			customerRepository.deleteById(username);
			log.info("Customer with username: " + username + " deleted");
		} catch (AuthenticatedPrincialException e) {
			log.error(
					"The username provided for deletion of a customer is the same as the current authenticated principal: "
							+ username);
			throw HttpError.badRequest(
					"You can't delete the customer you are currently logged as, with username: " + username);
		} catch (EmptyResultDataAccessException | NoSuchElementException e) {
			log.error("Couldn't delete customer. Customer not found with username: " + username, e);
			throw HttpError.notFound("Customer with username: " + username + " not found for deletion");
		} catch (Exception e) {
			log.error("Unknown error in deleting customer with username: " + username, e);
			throw HttpError.internalServerError(HttpError.ERROR_MSG_UNKNOWN);
		}
	}

	public ResponseEntity<Product> createProduct(@RequestBody Product newProduct) {
		try {
			boolean hasNullAttributes = productHasNullAttributes(newProduct);
			if (hasNullAttributes) {
				throw new NullAttributeException("At least one of the mandatory attributes is null");
			}
			Product product = productRepository.save(newProduct);
			log.info("New product created with id: " + product.getId());
			return ResponseEntity.ok(product);
		} catch (NullAttributeException e) {
			log.error("At least one of the mandatory attributes of product is null");
			throw HttpError.badRequest("Missing product data. Please fill all the mandatory fields");
		} catch (Exception e) {
			log.error("Unknown error in creating product", e);
			throw HttpError.internalServerError(HttpError.ERROR_MSG_UNKNOWN);
		}
	}

	public ResponseEntity<Product> updateProduct(@RequestBody Product productDetails, @PathVariable Long id) {
		try {
			boolean hasNullAttributes = productHasNullAttributes(productDetails);
			if (hasNullAttributes) {
				throw new NullAttributeException("At least one of the mandatory attributes is null");
			}
			Product updatedProduct = updateOrCreateProduct(productDetails, id);
			log.info("Product with id: " + updatedProduct.getId() + " updated/created");
			return ResponseEntity.ok(updatedProduct);

		} catch (NullAttributeException e) {
			log.error("At least one of the mandatory attributes of product is null");
			throw HttpError.badRequest("Missing product data. Please fill all the mandatory fields");
		} catch (Exception e) {
			log.error("Unknown error in updating product", e);
			throw HttpError.internalServerError(HttpError.ERROR_MSG_UNKNOWN);
		}

	}

	public void deleteProduct(@PathVariable Long id) {
		try {
			productRepository.deleteById(id);
			log.info("Product with id: " + id + " deleted");
		} catch (EmptyResultDataAccessException e) {
			log.error("Couldn't delete product. Product not found with id: " + id, e);
			throw HttpError.notFound("Couldn't delete product. Product not found");
		} catch (Exception e) {
			log.error("Unknown error in deleting product with id: " + id, e);
			throw HttpError.internalServerError(HttpError.ERROR_MSG_UNKNOWN);
		}
	}

	public ResponseEntity<Product> updateProductName(@PathVariable Long id, @PathVariable String newName) {
		try {
			Product updatedProduct = productRepository.findById(id).get();
			updatedProduct.setName(newName);
			productRepository.save(updatedProduct);
			log.info("Product name with id: " + id + " updated");
			return ResponseEntity.ok(updatedProduct);
		} catch (NoSuchElementException e) {
			log.error("Product not found with id: " + id, e);
			throw HttpError.notFound("Product not found");
		} catch (Exception e) {
			log.error("Unknown error in updating product with id: " + id, e);
			throw HttpError.internalServerError(HttpError.ERROR_MSG_UNKNOWN);
		}
	}

	public ResponseEntity<Product> updateProductCategory(@PathVariable Long id, @PathVariable Category newCategory) {
		try {
			Product updatedProduct = productRepository.findById(id).get();
			updatedProduct.setCategory(newCategory);
			productRepository.save(updatedProduct);
			log.info("Product category with id: " + id + " updated");
			return ResponseEntity.ok(updatedProduct);
		} catch (NoSuchElementException e) {
			log.error("Product not found with id: " + id, e);
			throw HttpError.notFound("Product not found");
		} catch (Exception e) {
			log.error("Unknown error in updating product with id: " + id, e);
			throw HttpError.internalServerError(HttpError.ERROR_MSG_UNKNOWN);
		}
	}

	public ResponseEntity<Product> updateProductPrice(@PathVariable Long id, @PathVariable double newPrice) {
		try {
			Product updatedProduct = productRepository.findById(id).get();
			updatedProduct.setPrice(newPrice);
			productRepository.save(updatedProduct);
			log.info("Product price with id: " + id + " updated");
			return ResponseEntity.ok(updatedProduct);
		} catch (NoSuchElementException e) {
			log.error("Product not found with id: " + id, e);
			throw HttpError.notFound("Product not found");
		} catch (Exception e) {
			log.error("Unknown error in updating product with id: " + id, e);
			throw HttpError.internalServerError(HttpError.ERROR_MSG_UNKNOWN);
		}
	}

	public ResponseEntity<List<Cart>> getCarts() {
		try {
			List<Cart> cartList = cartRepository.findAll();
			return ResponseEntity.ok(cartList);
		} catch (Exception e) {
			log.error("Unknown error in returning carts", e);
			throw HttpError.internalServerError(HttpError.ERROR_MSG_UNKNOWN);
		}
	}

	public ResponseEntity<Cart> getCartById(@PathVariable Long id) {
		try {
			Cart cart = cartRepository.findById(id).get();
			return ResponseEntity.ok(cart);
		} catch (NoSuchElementException e) {
			log.error("Cart not found with id: " + id, e);
			throw HttpError.notFound("Cart not found");
		} catch (Exception e) {
			log.error("Unknown error in returning cart with id: " + id, e);
			throw HttpError.internalServerError(HttpError.ERROR_MSG_UNKNOWN);
		}
	}

	public ResponseEntity<List<Role>> getRoles() {
		try {
			List<Role> roles = roleRepository.findAll();
			return ResponseEntity.ok(roles);
		} catch (Exception e) {
			log.error("Unknown error in returning roles", e);
			throw HttpError.internalServerError(HttpError.ERROR_MSG_UNKNOWN);
		}
	}

	public ResponseEntity<Role> createRole(@RequestBody Role newRole) {
		try {
			boolean hasNullAttributes = roleHasNullAttributes(newRole);
			if (hasNullAttributes) {
				throw new NullAttributeException("At least one of the mandatory attributes is null");
			}
			Role role = roleRepository.save(newRole);
			log.info("New role created with name: " + role.getRoleName());
			return ResponseEntity.ok(role);
		} catch (NullAttributeException e) {
			log.error("At least one of the mandatory attributes of role is null");
			throw HttpError.badRequest("Missing role data. Please fill all the mandatory fields");
		} catch (Exception e) {
			log.error("Unknown error in creating role", e);
			throw HttpError.internalServerError(HttpError.ERROR_MSG_UNKNOWN);
		}
	}

	// PRIVATE METHODS

	private boolean productHasNullAttributes(Product product) {
		if (product.getName() == null || product.getCategory() == null) {
			return true;
		}
		return false;
	}

	private boolean roleHasNullAttributes(Role role) {
		if (role.getRoleName() == null || role.getDescription() == null) {
			return true;
		}
		return false;
	}

	private Product updateOrCreateProduct(Product productDetails, Long id) {
		Product updatedProduct = productRepository.findById(id).map(prod -> {
			prod.setName(productDetails.getName());
			prod.setCategory(productDetails.getCategory());
			prod.setPrice(productDetails.getPrice());
			Product product = productRepository.save(prod);
			log.info("Product updated with id: " + product.getId());
			return product;
		}).orElseGet(() -> {
			Product product = productRepository.save(productDetails);
			log.warn("Product with id: " + id + " not found for updating");
			log.info("New product created with id: " + product.getId());
			return product;
		});
		return updatedProduct;
	}

}
