package com.alexdo97.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "identity")
public class Identity {

	@Id
	@Column(name = "username", unique = true, nullable = false)
	private String username;

	@Column(nullable = false)
	@JsonBackReference
	private String password;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "identity_role", joinColumns = @JoinColumn(name = "identity_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles;

//	@OneToOne(mappedBy = "identity", cascade = CascadeType.ALL, orphanRemoval = true)
//	@PrimaryKeyJoinColumn
//	@JsonManagedReference
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@MapsId
	@JoinColumn(name = "username", nullable = false)
	private Customer customer;

	public Identity() {

	}

	public Identity(String username, String password, List<Role> roles, Customer customer) {
		this.username = username;
		this.password = password;
		this.roles = roles;
		this.customer = customer;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

}
