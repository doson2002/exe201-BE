package com.exe201.exe201be.responses;

import com.exe201.exe201be.entities.FoodOrder;
import com.exe201.exe201be.entities.FoodOrderItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodOrderResponse {

    private Long id;
    @JsonProperty(value = "order_time")
    private Date orderTime;

    @JsonProperty(value = "pickup_time")
    private Date pickupTime;

    @JsonProperty(value = "pickup_location")
    private String pickupLocation;

    @JsonProperty(value = "status")
    private String status;   //('pending', 'confirmed', 'completed')

    @JsonProperty(value = "payment_method")
    private String paymentMethod;

    @JsonProperty(value = "payment_status")
    private int paymentStatus;

    @JsonProperty(value = "user_id")
    private Long userId;

    private List<FoodOrderItem> foodOrderItemsList;

    public static FoodOrderResponse fromFoodOrders(FoodOrder foodOrder, List<FoodOrderItem> foodOrderItems) {
        FoodOrderResponse orderResponse = FoodOrderResponse.builder()
                .id(foodOrder.getId())
                .orderTime(foodOrder.getOrderTime())
                .pickupTime(foodOrder.getPickupTime())
                .pickupLocation(foodOrder.getPickupLocation())
                .status(foodOrder.getStatus())
                .paymentMethod(foodOrder.getPaymentMethod())
                .userId(foodOrder.getUser().getId())
                .paymentStatus(foodOrder.getPaymentStatus())
                .foodOrderItemsList(foodOrderItems)
                .build();
        return orderResponse;
    }
}
