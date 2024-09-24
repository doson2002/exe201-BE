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

    @JsonProperty(value = "supplier_id")
    private Long supplierId;

    @JsonProperty(value = "rating_star")
    private int ratingStar;

    @JsonProperty(value = "response_message")
    private String responseMessage;

    @JsonProperty(value = "user_id")
    private Long userId;

}
