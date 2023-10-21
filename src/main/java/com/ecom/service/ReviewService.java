package com.ecom.service;

import java.util.List;

import com.ecom.exception.ProductException;
import com.ecom.model.Review;
import com.ecom.model.User;
import com.ecom.request.ReviewRequest;

public interface ReviewService {
	
	public Review createReive(ReviewRequest req, User user)throws ProductException;
	public List<Review> getAllReview(Long productId);

}
