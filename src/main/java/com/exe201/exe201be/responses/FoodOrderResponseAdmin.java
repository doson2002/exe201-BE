package com.exe201.exe201be.responses;

import com.exe201.exe201be.entities.FoodOrder;
import com.exe201.exe201be.entities.Users;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodOrderResponseAdmin {
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

    @JsonProperty(value = "customer")
    private Users customer;

    @JsonProperty(value = "supplier_name")
    private String supplierName;

    @JsonProperty(value = "supplier_image_url")
    private String supplierImageUrl;

    @JsonProperty(value = "total_items")
    private int totalItems;

    @JsonProperty(value = "total_price")
    private double totalPrice;

    @JsonProperty(value = "total_amount_order")
    private double totalAmountOrder ;

    @JsonProperty(value = "shipping_fee")
    private double shippingFee;

    public static FoodOrderResponseAdmin fromFoodOrders(FoodOrder foodOrder) {
        FoodOrderResponseAdmin orderResponse = FoodOrderResponseAdmin.builder()
                .id(foodOrder.getId())
                .supplierName(foodOrder.getSupplierInfo().getRestaurantName())
                .orderTime(foodOrder.getOrderTime())
                .pickupTime(foodOrder.getPickupTime())
                .pickupLocation(foodOrder.getPickupLocation())
                .status(foodOrder.getStatus())
                .paymentMethod(foodOrder.getPaymentMethod())
                .customer(foodOrder.getUser())
                .paymentStatus(foodOrder.getPaymentStatus())
                .supplierImageUrl(foodOrder.getSupplierInfo().getImgUrl())
                .totalPrice(foodOrder.getTotalPrice())
                .shippingFee(foodOrder.getShippingFee())
                .totalItems(foodOrder.getTotalItems())

                .build();
        return orderResponse;
    }
}
