package com.exe201.exe201be.services;

import com.exe201.exe201be.dtos.OrderRatingDTO;
import com.exe201.exe201be.entities.FoodOrder;
import com.exe201.exe201be.entities.OrderRating;
import com.exe201.exe201be.entities.SupplierInfo;
import com.exe201.exe201be.entities.Users;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.repositories.FoodOrderRepository;
import com.exe201.exe201be.repositories.OrderRatingRepository;
import com.exe201.exe201be.repositories.SupplierInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderRatingService implements IOrderRatingService{
    private final OrderRatingRepository orderRatingRepository;
    private final FoodOrderRepository foodOrderRepository;
    private final SupplierInfoRepository supplierInfoRepository;

    // Method to add a new rating and update the totalStarRating
    public OrderRating addRating(OrderRatingDTO orderRatingDTO) throws DataNotFoundException {
        FoodOrder existingFoodOrder = foodOrderRepository.findById(orderRatingDTO.getFoodOrderId())
                .orElseThrow(()-> new DataNotFoundException("Cannot find Food order with id: "+ orderRatingDTO.getFoodOrderId()));
        // Save the new rating
        OrderRating newOrderRating = new OrderRating();
        newOrderRating.setRatingStar(orderRatingDTO.getRatingStar());
        newOrderRating.setResponseMessage(orderRatingDTO.getResponseMessage());
        newOrderRating.setFoodOrder(existingFoodOrder);

        orderRatingRepository.save(newOrderRating);


        // Update the totalStarRating for the SupplierInfo associated with this order
        updateSupplierTotalStarRating(newOrderRating.getFoodOrder().getSupplierInfo().getId(), newOrderRating.getRatingStar());

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

}
