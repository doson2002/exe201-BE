package com.exe201.exe201be.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDTO {
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
}
