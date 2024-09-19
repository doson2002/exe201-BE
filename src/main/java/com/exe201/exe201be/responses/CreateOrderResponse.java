package com.exe201.exe201be.responses;

import com.exe201.exe201be.entities.FoodOrder;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderResponse {
    private String message;
    private FoodOrder foodOrder;
}
