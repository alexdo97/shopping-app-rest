package com.alexdo97.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alexdo97.model.ProductOrder;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {

	ProductOrder findByCartIdAndProductId(Long cartId, Long productId);
	
}
