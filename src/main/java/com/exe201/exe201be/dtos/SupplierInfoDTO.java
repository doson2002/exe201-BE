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
public class SupplierInfoDTO {
    @JsonProperty(value = "restaurant_name")
    private String restaurantName;

    @JsonProperty(value = "description")
    private  String description;

    @JsonProperty(value = "address")
    private String address;

    @JsonProperty(value = "img_url")
    private String imgUrl;

    @JsonProperty(value = "slogan")
    private String slogan;

    @JsonProperty(value = "user_id")
    private Long userId;
}
