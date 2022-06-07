package com.alexdo97.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alexdo97.model.Cart;
import com.alexdo97.model.ProductOrder;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

	Cart findByCustomerId(Long customerId);
}
