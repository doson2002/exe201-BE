package com.exe201.exe201be.services;

import com.exe201.exe201be.dtos.OrderRatingDTO;
import com.exe201.exe201be.entities.OrderRating;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.responses.SupplierRatingResponse;

import java.util.List;
import java.util.Map;

public interface IOrderRatingService {
    OrderRating addRating(OrderRatingDTO orderRatingDTO) throws DataNotFoundException;
    List<OrderRating> getRatingBySupplierId(Long supplierInfoId);
    SupplierRatingResponse getSupplierRatings(Long supplierId) throws DataNotFoundException;
    Map<Integer, List<String>> getAllResponseMessagesByStars(Long supplierId);
}
