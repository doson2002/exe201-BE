package com.exe201.exe201be.dtos;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRatingDTO {

    @JsonProperty(value = "food_order_id")
    private Long foodOrderId;

    @JsonProperty(value = "rating_star")
    private int ratingStar;

    @JsonProperty(value = "response_message")
    private String responseMessage;

}
