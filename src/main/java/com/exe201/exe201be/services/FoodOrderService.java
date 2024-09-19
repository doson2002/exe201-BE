package com.exe201.exe201be.services;

import com.exe201.exe201be.dtos.FoodOrderDTO;
import com.exe201.exe201be.dtos.OrderRequestDTO;
import com.exe201.exe201be.dtos.OrderUpdateRequestDTO;
import com.exe201.exe201be.entities.FoodItem;
import com.exe201.exe201be.entities.FoodOrder;
import com.exe201.exe201be.entities.FoodOrderItem;
import com.exe201.exe201be.entities.Users;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.repositories.FoodItemRepository;
import com.exe201.exe201be.repositories.FoodOrderItemRepository;
import com.exe201.exe201be.repositories.FoodOrderRepository;
import com.exe201.exe201be.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FoodOrderService implements IFoodOrderService{
    private final UserRepository userRepository;
    private final FoodItemRepository foodItemRepository;
    private final FoodOrderItemRepository foodOrderItemRepository;
    private final FoodOrderRepository foodOrderRepository;

    @Transactional
    public FoodOrder createOrder(List<OrderRequestDTO> orderRequests,
                                 FoodOrderDTO foodOrderDTO) throws DataNotFoundException {
        Users ExistingCustomer = userRepository.findById(foodOrderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Customer not found"));
        FoodOrder foodOrder = new FoodOrder();
        foodOrder.setOrderTime(foodOrderDTO.getOrderTime());
        foodOrder.setEstimatedPickupTime(foodOrderDTO.getEstimatedPickupTime());
        foodOrder.setPickupLocation(foodOrderDTO.getPickupLocation());
        foodOrder.setStatus(foodOrder.getStatus());
        foodOrder.setUser(ExistingCustomer);

        for (OrderRequestDTO orderRequest : orderRequests){
            FoodItem foodItem = foodItemRepository.findById(orderRequest.getFoodItemId())
                    .orElseThrow(() -> new RuntimeException("Food item not found"));
            FoodOrderItem foodOrderItem = new FoodOrderItem();
            foodOrderItem.setFoodOrder(foodOrder);
            foodOrderItem.setFoodItem(foodItem);
            foodOrderItem.setQuantity(orderRequest.getQuantity());
            // Update the food item stock
            var newQuantity = foodItem.getQuantity() - orderRequest.getQuantity();
            foodItem.setQuantity(newQuantity);
            foodOrderItemRepository.save(foodOrderItem);
        }
        return foodOrderRepository.save(foodOrder);
    }

    private void updateFoodOrderItemByOrderId(Long orderId,
                                            List<OrderRequestDTO> itemsToAdd,
                                            Map<Long, Integer> itemsToRemove) throws DataNotFoundException {
        FoodOrder order = foodOrderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order not found"));

        // Xử lý thêm sản phẩm từ danh sách OrderRequestDTO
        for (OrderRequestDTO itemToAdd : itemsToAdd) {
            FoodItem foodItem = foodItemRepository.findById(itemToAdd.getFoodItemId())
                    .orElseThrow(() -> new RuntimeException("Food item not found"));
            // Kiểm tra xem sản phẩm đã tồn tại trong chi tiết đơn hàng chưa
            FoodOrderItem existingFoodOrderItem= foodOrderItemRepository.findByFoodOrderId(orderId).stream()
                    .filter(detail -> detail.getFoodItem().getId().equals(foodItem.getId()))
                    .findFirst()
                    .orElse(null);
            // Nếu sản phẩm chưa tồn tại trong chi tiết đơn hàng, thêm mới
            if (existingFoodOrderItem==null) {
                FoodOrderItem foodOrderItem = new FoodOrderItem();
                foodOrderItem.setFoodOrder(order);
                foodOrderItem.setFoodItem(foodItem);
                foodOrderItem.setQuantity(itemToAdd.getQuantity());

                foodOrderItemRepository.save(foodOrderItem);
            } else {
                // Nếu sản phẩm đã tồn tại, cập nhật số lượng
                existingFoodOrderItem.setQuantity(existingFoodOrderItem.getQuantity() + itemToAdd.getQuantity());
            }

            // Xử lý xóa sản phẩm từ danh sách productsToRemove
            for (Map.Entry<Long, Integer> entry : itemsToRemove.entrySet()) {
                Long itemIdToRemove = entry.getKey();
                Integer quantityToRemove = entry.getValue();

                // Tìm chi tiết đơn hàng tương ứng với sản phẩm cần xóa
                Optional<FoodOrderItem> orderDetailOptional = foodOrderItemRepository.findByFoodOrderId(orderId).stream()
                        .filter(detail -> detail.getFoodItem().getId().equals(itemIdToRemove))
                        .findFirst();

                // Nếu sản phẩm tồn tại trong đơn hàng, cập nhật số lượng hoặc xóa nếu số lượng còn lại là 0
                if (orderDetailOptional.isPresent()) {
                    FoodOrderItem foodOrderItem = orderDetailOptional.get();
                    int remainingQuantity = foodOrderItem.getQuantity() - quantityToRemove;
                    if (remainingQuantity > 0) {
                        foodOrderItem.setQuantity(remainingQuantity);
                    } else {
                        foodOrderItemRepository.delete(foodOrderItem);
                    }
                }
            }
        }
    }

    public List<FoodOrder> getAllFoodOrders(){
        return foodOrderRepository.findAll();
    }

    public FoodOrder getFoodOrderById(Long id) throws DataNotFoundException {
        return foodOrderRepository.findById(id).orElseThrow(()->new DataNotFoundException("Cannot find Food Order with id: "+ id));
    }

    public List<FoodOrder> getFoodOrdersByUserId(Long userId){
        return foodOrderRepository.findByUser_Id(userId);
    }
    @Transactional
    public void updateOrderStatus(long orderId,String orderStatus) throws DataNotFoundException {
        FoodOrder existingOrder = foodOrderRepository.findById(orderId)
                .orElseThrow(()-> new DataNotFoundException("Food order not found"));
        existingOrder.setStatus(orderStatus);
    }
    private void updateFoodOrder(Long foodOrderId, OrderUpdateRequestDTO updateRequestDTO) throws DataNotFoundException {
        FoodOrder existingOrder = foodOrderRepository.findById(foodOrderId)
                .orElseThrow(()->new DataNotFoundException("Food order cannot find with id: "+ foodOrderId));

        // Cập nhật các thuộc tính đơn hàng
        if (updateRequestDTO.getStatus() != null) {
            existingOrder.setStatus(updateRequestDTO.getStatus());
        }

        if (updateRequestDTO.getPaymentMethod() != null) {
            existingOrder.setPaymentMethod(updateRequestDTO.getPaymentMethod());
        }

        if (updateRequestDTO.getPickupLocation() != null) {
            existingOrder.setPickupLocation(updateRequestDTO.getPickupLocation());
        }

        if (updateRequestDTO.getPaymentStatus() != null) {
            existingOrder.setPaymentStatus(updateRequestDTO.getPaymentStatus());
        }
        if (updateRequestDTO.getEstimatedPickupTime() != null) {
            existingOrder.setEstimatedPickupTime(updateRequestDTO.getEstimatedPickupTime());
        }


        foodOrderRepository.save(existingOrder);
    }



}
