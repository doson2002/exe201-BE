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
public class FoodItemReportResponse {
    private Long id;
    @JsonProperty(value = "food_name")
    private String foodName;

    @JsonProperty(value = "image_url")
    private String imageUrl;

    @JsonProperty(value = "quantity_sold")
    private  int quantitySold;

    public static FoodItemReportResponse fromFoodItem(FoodItem foodItem) {

        FoodItemReportResponse foodItemReportResponse = FoodItemReportResponse.builder()
                .id(foodItem.getId())
                .foodName(foodItem.getFoodName())
                .quantitySold(foodItem.getQuantitySold())
                .imageUrl(foodItem.getImgUrl())
                .build();
        return foodItemReportResponse;
    }
}
