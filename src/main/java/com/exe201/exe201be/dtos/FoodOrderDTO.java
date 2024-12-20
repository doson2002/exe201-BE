package com.exe201.exe201be.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.*;

import java.util.Date;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FoodOrderDTO {
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

    @JsonProperty(value = "shipping_fee")
    private double shippingFee;

    @JsonProperty(value = "user_id")
    private Long userId;

    @JsonProperty(value = "supplier_id")
    private Long supplierId;


}
