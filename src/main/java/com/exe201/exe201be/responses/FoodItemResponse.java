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
public class FoodItemResponse {

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

    @JsonProperty(value = "supplier_info")
    private SupplierInfo supplierInfo;

    @JsonProperty(value = "food_types")
    private List<FoodType> foodTypes;

    public static FoodItemResponse fromFoodItem(FoodItem foodItem) {

        // Lấy danh sách FoodType từ FoodItemType
        List<FoodType> foodTypesList = foodItem.getFoodItemTypes().stream()
                .map(FoodItemType::getFoodType)
                .collect(Collectors.toList());

        FoodItemResponse foodItemResponse = FoodItemResponse.builder()
                .id(foodItem.getId())
                .foodName(foodItem.getFoodName())
                .price(foodItem.getPrice())
                .quantitySold(foodItem.getQuantitySold())
                .supplierInfo(foodItem.getSupplierInfo())
                .description(foodItem.getDescription())
                .imageUrl(foodItem.getImgUrl())
                .status(foodItem.getStatus())
                .readyTime(foodItem.getReadyTime())
                .category(foodItem.getCategory())
                .foodTypes(foodTypesList)
                .build();
        return foodItemResponse;
    }
}
