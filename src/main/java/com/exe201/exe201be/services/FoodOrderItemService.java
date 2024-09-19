package com.exe201.exe201be.services;

import com.exe201.exe201be.entities.FoodOrderItem;
import com.exe201.exe201be.repositories.FoodOrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodOrderItemService implements IFoodOrderItemService{
    private final FoodOrderItemRepository foodOrderItemRepository;

    public List<FoodOrderItem> getFoodOrderItemByOrderId(Long orderId) {
        return foodOrderItemRepository.findByFoodOrderId(orderId);
    }
}
