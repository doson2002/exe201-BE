package com.exe201.exe201be.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class FoodOrderResponseList {
    private List<FoodOrderResponse> orders;
}
