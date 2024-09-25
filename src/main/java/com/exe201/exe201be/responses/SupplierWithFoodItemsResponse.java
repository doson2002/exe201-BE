package com.exe201.exe201be.responses;

import com.exe201.exe201be.entities.SupplierInfo;
import lombok.*;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class SupplierWithFoodItemsResponse {

    private SupplierInfo supplierInfo;
    private List<FoodItemResponseWithSupplier> foodItems;
}
