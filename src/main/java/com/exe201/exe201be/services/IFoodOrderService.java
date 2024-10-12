package com.exe201.exe201be.services;

import com.exe201.exe201be.dtos.FoodOrderDTO;
import com.exe201.exe201be.dtos.OrderRequestDTO;
import com.exe201.exe201be.entities.FoodOrder;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.responses.FoodOrderDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IFoodOrderService {

    FoodOrder createOrder(List<OrderRequestDTO> orderRequests,
                          FoodOrderDTO foodOrderDTO) throws DataNotFoundException;
    List<FoodOrder> getAllFoodOrders();
    FoodOrderDetailResponse getFoodOrderDetailById(Long id) throws DataNotFoundException;

    List<FoodOrder> getFoodOrdersByUserId(Long userId);
    Page<FoodOrder> getFoodOrdersByUserIdPaging(Long userId, String status, Date startDate, Date endDate, Pageable pageable);
    void updateOrderStatus(long orderId,String orderStatus) throws DataNotFoundException;
    void updatePaymentStatus(long orderId,int paymentStatus) throws DataNotFoundException;

    void deleteOrder(long orderId) throws DataNotFoundException;

}
