package com.exe201.exe201be.dtos;

import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDetailDTO {
    private List<OrderRequestDTO> orderRequests;
    private FoodOrderDTO foodOrderDTO;
}
