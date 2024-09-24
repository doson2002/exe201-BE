package com.exe201.exe201be.services;

import com.exe201.exe201be.dtos.OrderRatingDTO;
import com.exe201.exe201be.entities.*;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.repositories.FoodOrderRepository;
import com.exe201.exe201be.repositories.OrderRatingRepository;
import com.exe201.exe201be.repositories.SupplierInfoRepository;
import com.exe201.exe201be.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderRatingService implements IOrderRatingService{
    private final OrderRatingRepository orderRatingRepository;

    private final SupplierInfoRepository supplierInfoRepository;

    private final UserRepository userRepository;

    // Method to add a new rating and update the totalStarRating
    public OrderRating addRating(OrderRatingDTO orderRatingDTO) throws DataNotFoundException {
        SupplierInfo existingSupplier = supplierInfoRepository.findById(orderRatingDTO.getSupplierId())
                .orElseThrow(()-> new DataNotFoundException("Cannot find Supplier with id: "+ orderRatingDTO.getSupplierId()));
        Users existingUser = userRepository.findById(orderRatingDTO.getUserId())
                .orElseThrow(()-> new DataNotFoundException("Cannot find User with id: "+ orderRatingDTO.getUserId()));
        // Save the new rating
        OrderRating newOrderRating = new OrderRating();
        newOrderRating.setRatingStar(orderRatingDTO.getRatingStar());
        newOrderRating.setResponseMessage(orderRatingDTO.getResponseMessage());
        newOrderRating.setSupplierInfo(existingSupplier);
        newOrderRating.setUsers(existingUser);

        orderRatingRepository.save(newOrderRating);


        // Update the totalStarRating for the SupplierInfo associated with this order
        updateSupplierTotalStarRating(newOrderRating.getSupplierInfo().getId(), newOrderRating.getRatingStar());

        return newOrderRating;
    }

    private void updateSupplierTotalStarRating(Long supplierInfoId, int newRating) throws DataNotFoundException {
        // Fetch the supplier info
        SupplierInfo supplierInfo = supplierInfoRepository.findById(supplierInfoId)
                .orElseThrow(() -> new DataNotFoundException("SupplierInfo not found for this id :: " + supplierInfoId));

        // Calculate the new totalStarRating
        double currentTotalStarRating = supplierInfo.getTotalStarRating();
        int currentReviewCount = supplierInfo.getTotalReviewCount();

        double newTotalStarRating = (currentTotalStarRating * currentReviewCount + newRating) / (currentReviewCount + 1);

        // Update supplier info
        supplierInfo.setTotalStarRating(newTotalStarRating);
        supplierInfo.setTotalReviewCount(currentReviewCount + 1);
        supplierInfoRepository.save(supplierInfo);
    }

    public List<OrderRating> getRatingBySupplierId(Long supplierInfoId) {
        return orderRatingRepository.findBySupplierInfo_Id(supplierInfoId);
    }

}
