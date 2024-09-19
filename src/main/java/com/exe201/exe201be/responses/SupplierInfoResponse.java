package com.exe201.exe201be.responses;

import com.exe201.exe201be.entities.SupplierInfo;
import com.exe201.exe201be.entities.Users;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierInfoResponse {

    private Long id;

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
    private Users user;

    public static SupplierInfoResponse fromSupplierInfo(SupplierInfo supplierInfo) {
        SupplierInfoResponse supplierInfoResponse = SupplierInfoResponse.builder()
                .id(supplierInfo.getId())
                .restaurantName(supplierInfo.getRestaurantName())
                .description(supplierInfo.getDescription())
                .slogan(supplierInfo.getSlogan())
                .address(supplierInfo.getAddress())
                .imgUrl(supplierInfo.getImgUrl())
                .user(supplierInfo.getUser())
                .build();
        return supplierInfoResponse;
    }
}
