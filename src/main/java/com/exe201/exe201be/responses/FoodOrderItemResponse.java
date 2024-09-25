package com.exe201.exe201be.responses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodOrderItemResponse {

    private Long id;
    private String foodName;
    private int quantity;
}
