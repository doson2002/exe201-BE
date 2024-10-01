package com.exe201.exe201be.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalTime;
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDTO {

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

    @JsonProperty(value = "supplier_type_id")
    private Long supplierTypeId;

    @JsonProperty(value = "open_time")
    private LocalTime openTime;

    @JsonProperty(value = "close_time")
    private LocalTime closeTime;
}
