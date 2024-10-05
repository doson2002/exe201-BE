package com.exe201.exe201be.responses;


import com.exe201.exe201be.entities.FoodItem;
import com.exe201.exe201be.entities.FoodItemType;
import com.exe201.exe201be.entities.FoodType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodItemOfferedResponse {
    private Long id;
    @JsonProperty(value = "food_name")
    private String foodName;

    private String description;

    @JsonProperty(value = "price")
    private double price;

    @JsonProperty(value = "image_url")
    private String imageUrl;

    private int quantity;

    @JsonProperty(value = "supplier_id")
    private Long supplierId;

    public static FoodItemOfferedResponse fromFoodItem(FoodItem foodItem) {

        FoodItemOfferedResponse foodItemOfferedResponse = FoodItemOfferedResponse.builder()
                .id(foodItem.getId())
                .foodName(foodItem.getFoodName())
                .price(foodItem.getPrice())
                .description(foodItem.getDescription())
                .imageUrl(foodItem.getImgUrl())
                .quantity(1)
                .supplierId(foodItem.getSupplierInfo().getId())
                .build();
        return foodItemOfferedResponse;
    }
}
