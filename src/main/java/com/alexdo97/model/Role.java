package com.alexdo97.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class Role {

	@Id
	@Column(name = "role_name", nullable = false)
	private String roleName;

	@Column(nullable = false)
	private String description;

	@ManyToMany(mappedBy = "roleList", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH })
	private List<Identity> identityList;

	public Role() {

	}

	public Role(String roleName, String description) {
		this.roleName = roleName;
		this.description = description;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
