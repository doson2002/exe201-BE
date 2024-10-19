package com.exe201.exe201be.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PromotionsDTO {
    @JsonProperty("discount_percentage")
    private double discountPercentage;
    @JsonProperty(value = "fixed_discount_amount")
    private double fixedDiscountAmount;

    private String description;

    @JsonProperty("supplier_id")
    private Long supplierId;

    private boolean status;

    @JsonProperty("promotion_type")
    private int promotionType;

}
