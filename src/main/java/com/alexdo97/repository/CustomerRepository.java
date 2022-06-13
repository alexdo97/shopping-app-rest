package com.alexdo97.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alexdo97.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

	Optional<Customer> findByEmail(String email);

	Optional<Customer> findByPhoneNumber(String phoneNumber);
}
