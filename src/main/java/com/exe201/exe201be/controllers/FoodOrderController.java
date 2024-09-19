package com.exe201.exe201be.controllers;

import com.exe201.exe201be.dtos.OrderRequestDetailDTO;
import com.exe201.exe201be.entities.FoodOrder;
import com.exe201.exe201be.entities.FoodOrderItem;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.repositories.FoodOrderItemRepository;
import com.exe201.exe201be.repositories.FoodOrderRepository;
import com.exe201.exe201be.responses.CreateOrderResponse;
import com.exe201.exe201be.responses.FoodOrderResponse;
import com.exe201.exe201be.responses.FoodOrderResponseList;
import com.exe201.exe201be.services.IFoodOrderItemService;
import com.exe201.exe201be.services.IFoodOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/food_orders")
@RequiredArgsConstructor
public class FoodOrderController {
    private final IFoodOrderService foodOrderService;
    private final FoodOrderRepository foodOrderRepository;
    private final IFoodOrderItemService foodOrderItemService;
    private final FoodOrderItemRepository foodOrderItemRepository;


    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF','ROLE_MANAGER')")
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequestDetailDTO request,
                                         BindingResult result) {

        try {
            if(result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            FoodOrder foodOrder = foodOrderService.createOrder(
                    request.getOrderRequests(),
                    request.getFoodOrderDTO()
            );
            CreateOrderResponse response = new CreateOrderResponse("Order Created successfully", foodOrder);
            return ResponseEntity.ok(response);
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get_food_order_by_id/{orderId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER','ROLE_STAFF')")
    public ResponseEntity<?> getFoodOrder(@Valid @PathVariable Long orderId) throws DataNotFoundException {
        FoodOrder foodOrder = foodOrderService.getFoodOrderById(orderId);
        List<FoodOrderItem> foodOrderItemList = foodOrderItemRepository.findByFoodOrderId(foodOrder.getId());
        return ResponseEntity.ok(FoodOrderResponse.fromFoodOrders(foodOrder, foodOrderItemList));
    }

    @GetMapping("/get_all_food_orders")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF','ROLE_MANAGER')")
    public ResponseEntity<FoodOrderResponseList> getAllOrders() {

        List<FoodOrder> orders = foodOrderService.getAllFoodOrders();

        return getFoodOrderResponseListResponseEntity(orders);
    }

    @GetMapping("/get_food_order_by_userId/{userId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<FoodOrderResponseList> getFoodOrdersByUser(@Valid @PathVariable Long userId) {

        List<FoodOrder> orders = foodOrderService.getFoodOrdersByUserId(userId);

        return getFoodOrderResponseListResponseEntity(orders);
    }

    @PutMapping("/update_order_status/{orderId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MANAGER','ROLE_STAFF')")
    public ResponseEntity<?> updateFoodOrderStatus(@Valid @PathVariable Long orderId,
                                                   @RequestBody String orderStatus){
        try {
            foodOrderService.updateOrderStatus(orderId, orderStatus);
            return ResponseEntity.ok("order status updated successfully.");
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    private ResponseEntity<FoodOrderResponseList> getFoodOrderResponseListResponseEntity(List<FoodOrder> orders) {
        List<FoodOrderResponse> orderResponses = orders.stream()
                .map(order -> {
                    List<FoodOrderItem> orderItems = foodOrderItemRepository.findByFoodOrderId(order.getId());
                    return FoodOrderResponse.fromFoodOrders(order, orderItems);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(FoodOrderResponseList.builder()
                .orders(orderResponses)
                .build());
    }

}
