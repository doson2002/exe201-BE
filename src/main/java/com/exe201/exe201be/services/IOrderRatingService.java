package com.exe201.exe201be.services;

import com.exe201.exe201be.dtos.OrderRatingDTO;
import com.exe201.exe201be.entities.OrderRating;
import com.exe201.exe201be.exceptions.DataNotFoundException;

public interface IOrderRatingService {
    OrderRating addRating(OrderRatingDTO orderRatingDTO) throws DataNotFoundException;
}
