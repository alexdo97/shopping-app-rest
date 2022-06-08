package com.alexdo97.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alexdo97.model.Identity;

@Repository
public interface IdentityRepository extends JpaRepository<Identity, String> {

}
