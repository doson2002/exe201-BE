package com.exe201.exe201be.responses;

import com.exe201.exe201be.entities.SupplierInfo;
import com.exe201.exe201be.entities.SupplierType;
import com.exe201.exe201be.entities.Users;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalTime;

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

    @JsonProperty(value = "total_star_rating")
    private double totalStarRating;

    @JsonProperty(value = "total_review_count")
    private int totalReviewCount;

    @JsonProperty(value = "supplier_type")
    private SupplierType supplierType;

    @JsonProperty(value = "user_id")
    private Users user;

    @JsonProperty(value = "close_time")
    private LocalTime closeTime;

    @JsonProperty(value = "open_time")
    private LocalTime openTime;

    // Thêm biến để lưu tọa độ
    @JsonProperty(value = "latitude")
    private double latitude;

    @JsonProperty(value = "longitude")
    private double longitude;

    private int status;



    public static SupplierInfoResponse fromSupplierInfo(SupplierInfo supplierInfo) {
        SupplierInfoResponse supplierInfoResponse = SupplierInfoResponse.builder()
                .id(supplierInfo.getId())
                .restaurantName(supplierInfo.getRestaurantName())
                .description(supplierInfo.getDescription())
                .totalReviewCount(supplierInfo.getTotalReviewCount())
                .totalStarRating(supplierInfo.getTotalStarRating())
                .supplierType(supplierInfo.getSupplierType())
                .address(supplierInfo.getAddress())
                .imgUrl(supplierInfo.getImgUrl())
                .closeTime(supplierInfo.getCloseTime())
                .openTime(supplierInfo.getOpenTime())
                .latitude(supplierInfo.getLatitude())
                .longitude(supplierInfo.getLongitude())
                .status(supplierInfo.getStatus())
                .user(supplierInfo.getUser())
                .build();
        return supplierInfoResponse;
    }
}
