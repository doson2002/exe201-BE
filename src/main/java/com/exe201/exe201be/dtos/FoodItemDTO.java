package com.exe201.exe201be.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FoodItemDTO {

    @JsonProperty(value = "food_name")
    private String foodName;

    @JsonProperty(value = "quantity_sold")
    private  int quantitySold;

    @JsonProperty(value = "price")
    private double price;

    @JsonProperty(value = "description")
    private String description;

    @JsonProperty(value = "status")
    private String status;   //('available', 'pending', 'collected')

    @JsonProperty(value = "image_url")
    private String imageUrl;

    @JsonProperty(value = "ready_time")
    private Date readyTime;

    @JsonProperty(value = "category")
    private String category;//("fee required","free")

    @JsonProperty(value = "supplier_id")
    private Long supplierId;

    @JsonProperty(value = "food_type_ids")
    private List<Long> foodTypeIds; // List of FoodType IDs



}
