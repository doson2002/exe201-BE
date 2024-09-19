package com.exe201.exe201be.responses;

import com.exe201.exe201be.entities.Delivery;
import com.exe201.exe201be.entities.FoodItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryResponse {
    private Long id;
    @JsonProperty(value = "driver_name")
    private String driverName;

    @JsonProperty(value = "vehicle_type")
    private String vehicleType;

    @JsonProperty(value = "phone")
    private String phone;

    @JsonProperty(value = "address")
    private String address;

    @JsonProperty(value = "is_active")
    private Boolean isActive;

    public static DeliveryResponse fromDelivery(Delivery delivery) {
        DeliveryResponse deliveryResponse = DeliveryResponse.builder()
                .id(delivery.getId())
                .driverName(delivery.getDriverName())
                .vehicleType(delivery.getVehicleType())
                .address(delivery.getAddress())
                .phone(delivery.getPhone())
                .isActive(delivery.getIsActive())
                .build();
        return deliveryResponse;
    }
}
