package com.exe201.exe201be.dtos;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FoodItemOrderDTO {
    private Long foodId;
    private String name;
    private int quantitySold;
    private SupplierDTO supplierInfo;
}
