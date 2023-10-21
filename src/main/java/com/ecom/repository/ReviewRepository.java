package com.ecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecom.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	@Query("Select r from Review r where r.product.id=:productId")
	public List<Review>getAllProductsRevew(@Param("productId")Long productId);
}
