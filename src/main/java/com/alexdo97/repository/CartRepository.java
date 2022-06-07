package com.alexdo97.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alexdo97.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

}
