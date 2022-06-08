package com.alexdo97.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "product_order")
public class ProductOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cart_id", nullable = false)
	@JsonIgnore
	private Cart cart;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	private int quantity = 1;

	@Column(name = "registered_at")
	private LocalDateTime registeredAt;

	public ProductOrder() {
	}

	public ProductOrder(Product product, Cart cart, int quantity) {
		this.product = product;
		this.cart = cart;
		this.quantity = quantity;
		this.registeredAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public LocalDateTime getRegisteredAt() {
		return registeredAt;
	}

	public void setRegisteredAt(LocalDateTime registeredAt) {
		this.registeredAt = registeredAt;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public String toString() {
		return "ProductOrder [id=" + id + ", cart=" + cart + ", product=" + product + ", quantity=" + quantity
				+ ", registeredAt=" + registeredAt + "]";
	}

}
