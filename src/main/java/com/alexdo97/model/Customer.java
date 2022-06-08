package com.alexdo97.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "customer")
public class Customer {

	@Id
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "last_name", nullable = false)
	private String firstName;

	@Column(name = "first_name", nullable = false)
	private String lastName;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(unique = true, nullable = false)
	private String phoneNumber;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@MapsId
	@JoinColumn(name = "id", nullable = false)
	@JsonBackReference
	private Identity identity;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "cart_id", referencedColumnName = "id")
	@JsonManagedReference
	private Cart cart;

	public Customer() {

	}

	public Customer(String firstName, String lastName, String email, String phoneNumber, Identity identity, Cart cart) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.identity = identity;
		this.cart = cart;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Identity getIdentity() {
		return identity;
	}

	public void setIdentity(Identity identity) {
		this.identity = identity;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

}
