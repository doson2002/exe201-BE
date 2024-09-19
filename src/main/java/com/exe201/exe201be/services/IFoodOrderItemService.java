package com.exe201.exe201be.services;


import com.exe201.exe201be.entities.FoodOrderItem;

import java.util.List;

public interface IFoodOrderItemService {
    List<FoodOrderItem> getFoodOrderItemByOrderId(Long orderId);
}
