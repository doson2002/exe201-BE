package com.exe201.exe201be.services;

import com.exe201.exe201be.dtos.FoodOrderDTO;
import com.exe201.exe201be.dtos.OrderRequestDTO;
import com.exe201.exe201be.entities.FoodOrder;
import com.exe201.exe201be.exceptions.DataNotFoundException;

import java.util.List;

public interface IFoodOrderService {

    FoodOrder createOrder(List<OrderRequestDTO> orderRequests,
                          FoodOrderDTO foodOrderDTO) throws DataNotFoundException;
    List<FoodOrder> getAllFoodOrders();
    FoodOrder getFoodOrderById(Long id) throws DataNotFoundException;

    List<FoodOrder> getFoodOrdersByUserId(Long userId);

    void updateOrderStatus(long orderId,String orderStatus) throws DataNotFoundException;
}
