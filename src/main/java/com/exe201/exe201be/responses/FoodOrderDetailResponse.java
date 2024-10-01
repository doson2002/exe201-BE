package com.exe201.exe201be.responses;

import com.exe201.exe201be.entities.FoodOrder;
import com.exe201.exe201be.entities.SupplierInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodOrderDetailResponse {
    private Long id;

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

    @JsonProperty(value = "supplier_info")
    private SupplierInfo supplierInfo;

    private List<FoodOrderItemResponse> foodOrderItemResponseList;



    public static FoodOrderDetailResponse fromFoodOrders(FoodOrder foodOrder, List<FoodOrderItemResponse> foodOrderItemResponseList) {
        FoodOrderDetailResponse orderResponse = FoodOrderDetailResponse.builder()
                .id(foodOrder.getId())
                .pickupTime(foodOrder.getPickupTime())
                .pickupLocation(foodOrder.getPickupLocation())
                .status(foodOrder.getStatus())
                .paymentMethod(foodOrder.getPaymentMethod())
                .paymentStatus(foodOrder.getPaymentStatus())
                .supplierInfo(foodOrder.getSupplierInfo())
                .foodOrderItemResponseList(foodOrderItemResponseList)

                .build();
        return orderResponse;
    }
}
