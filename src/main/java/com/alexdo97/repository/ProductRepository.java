package com.alexdo97.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alexdo97.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
