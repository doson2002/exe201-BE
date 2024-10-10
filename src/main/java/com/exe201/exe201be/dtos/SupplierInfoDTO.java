package com.exe201.exe201be.dtos;

import com.exe201.exe201be.entities.SupplierType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalTime;

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

    @JsonProperty(value = "total_star_rating")
    private double totalStarRating;

    @JsonProperty(value = "total_review_count")
    private int totalReviewCount;

    @JsonProperty(value = "supplier_type_id")
    private Long supplierTypeId;

    @JsonProperty(value = "open_time")
    private LocalTime openTime;

    @JsonProperty(value = "close_time")
    private LocalTime closeTime;
    @JsonProperty(value = "user_id")
    private Long userId;

    // Thêm biến để lưu tọa độ
    @JsonProperty(value = "latitude")
    private double latitude;

    @JsonProperty(value = "longitude")
    private double longitude;
}
