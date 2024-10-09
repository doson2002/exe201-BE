package com.exe201.exe201be.responses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodItemSoldTopResponse {
    private Long id;
    private String foodName;
    private int quantitySold;
    private int inventoryQuantity;
    private double percentageSold;
}
