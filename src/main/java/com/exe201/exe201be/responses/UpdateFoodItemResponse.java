package com.exe201.exe201be.responses;

import com.exe201.exe201be.entities.FoodItem;
import com.exe201.exe201be.entities.FoodItemType;
import com.exe201.exe201be.entities.FoodType;
import com.exe201.exe201be.entities.SupplierInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateFoodItemResponse {
    private Long id;
    @JsonProperty(value = "food_name")
    private String foodName;

    private String description;

    @JsonProperty(value = "quantity_sold")
    private  int quantitySold;

    @JsonProperty(value = "price")
    private double price;

    @JsonProperty(value = "status")
    private String status;   //('available', 'pending', 'collected')

    @JsonProperty(value = "image_url")
    private String imageUrl;

    @JsonProperty(value = "ready_time")
    private Date readyTime;

    @JsonProperty(value = "category")
    private String category;//("fee required","free")



    public static UpdateFoodItemResponse fromUpdateFoodItem(FoodItem foodItem) {


        UpdateFoodItemResponse updateFoodItemResponse = UpdateFoodItemResponse.builder()
                .id(foodItem.getId())
                .foodName(foodItem.getFoodName())
                .price(foodItem.getPrice())
                .quantitySold(foodItem.getQuantitySold())
                .description(foodItem.getDescription())
                .imageUrl(foodItem.getImgUrl())
                .status(foodItem.getStatus())
                .readyTime(foodItem.getReadyTime())
                .category(foodItem.getCategory())
                .build();
        return updateFoodItemResponse;
    }
}
