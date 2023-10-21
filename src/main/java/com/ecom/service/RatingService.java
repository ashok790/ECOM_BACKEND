package com.ecom.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecom.exception.ProductException;
import com.ecom.model.Rating;
import com.ecom.model.User;
import com.ecom.request.RatingRequest;

public interface RatingService {
	
	public Rating createRating(RatingRequest req, User user) throws ProductException;
	public List<Rating> getProdutsRating(Long productId);
	

}
