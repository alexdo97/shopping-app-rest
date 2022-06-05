package com.alexdo97.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "customer")
public class Customer {

	@Id
	@Column(name = "identity_id")
	private Long id;

	@Column(name = "last_name")
	private String firstName;

	@Column(name = "first_name")
	private String lastName;

	@Column(unique = true)
	private String email;

	@OneToOne(cascade = CascadeType.ALL)
	@MapsId
	@JoinColumn(name = "identity_id")
	private Identity identity;

	public Customer() {
		
	}

	public Customer(String firstName, String lastName, String email, Identity identity) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.identity = identity;
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
	
}
