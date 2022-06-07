package com.alexdo97.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "cart")
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(mappedBy = "cart", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Customer customer;

	@OneToMany(mappedBy = "cart", fetch = FetchType.LAZY)
	private List<ProductOrder> productOrders;

	private double total;

	public Cart() {
		this.total = productOrders.stream().mapToDouble(p -> p.getProduct().getPrice() * p.getQuantity()).sum();
	}

	public Cart(Customer customer, List<ProductOrder> productOrders) {
		this.customer = customer;
		this.productOrders = productOrders;
		this.total = productOrders.stream().mapToDouble(p -> p.getProduct().getPrice() * p.getQuantity()).sum();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<ProductOrder> getProductOrders() {
		return productOrders;
	}

	public void setProductOrders(List<ProductOrder> productOrders) {
		this.productOrders = productOrders;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public void calculateTotal() {
		this.total = productOrders.stream().mapToDouble(p -> p.getProduct().getPrice() * p.getQuantity()).sum();
	}

}
