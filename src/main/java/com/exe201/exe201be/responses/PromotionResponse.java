package com.exe201.exe201be.responses;

import com.exe201.exe201be.entities.OrderRating;
import com.exe201.exe201be.entities.Promotions;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionResponse {
    private Long id;

    private String code;
    @JsonProperty("discount_percentage")
    private double discountPercentage;
    @JsonProperty(value = "fixed_discount_amount")
    private double fixedDiscountAmount;

    private String description;

    @JsonProperty("supplier_id")
    private Long supplierId;

    private boolean status;

    public static PromotionResponse fromPromotion(Promotions promotions) {
        PromotionResponse promotionResponse = PromotionResponse.builder()
                .id(promotions.getId())
                .code(promotions.getCode())
                .description(promotions.getDescription())
                .discountPercentage(promotions.getDiscountPercentage())
                .fixedDiscountAmount(promotions.getFixedDiscountAmount())
                .supplierId(promotions.getSupplierInfo().getId())
                .status(promotions.isStatus())
                .build();
        return promotionResponse;
    }

}
