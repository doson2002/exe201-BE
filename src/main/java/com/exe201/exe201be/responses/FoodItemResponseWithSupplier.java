package com.exe201.exe201be.responses;
import com.exe201.exe201be.entities.FoodItem;
import com.exe201.exe201be.entities.FoodItemType;
import com.exe201.exe201be.entities.FoodType;
import com.exe201.exe201be.entities.SupplierInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodItemResponseWithSupplier {

    private Long id;
    @JsonProperty(value = "food_name")
    private String foodName;

    @JsonProperty(value = "price")
    private double price;

    @JsonProperty(value = "image_url")
    private String imageUrl;

    @JsonProperty(value = "quantity_add")
    private int quantityAdd;

    @JsonProperty(value = "supplierInfo")
    private SupplierInfo supplierInfo;

    public static FoodItemResponseWithSupplier fromFoodItem(FoodItem foodItem) {


        FoodItemResponseWithSupplier foodItemResponseWithSupplier = FoodItemResponseWithSupplier.builder()
                .id(foodItem.getId())
                .foodName(foodItem.getFoodName())
                .price(foodItem.getPrice())
                .imageUrl(foodItem.getImgUrl())
                .quantityAdd(1)
                .supplierInfo(foodItem.getSupplierInfo())
                .build();
        return foodItemResponseWithSupplier;
    }
}
