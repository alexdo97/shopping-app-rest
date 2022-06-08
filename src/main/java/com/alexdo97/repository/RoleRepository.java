package com.alexdo97.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alexdo97.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findByRoleName(String roleName);
}
