package com.exe201.exe201be.responses;

import com.exe201.exe201be.entities.OrderRating;
import com.exe201.exe201be.entities.Users;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRatingResponse {
    private Long id;

    @JsonProperty(value = "rating_star")
    private int ratingStar;

    @JsonProperty(value = "response_message")
    private String responseMessage;

    private Users users;


    public static OrderRatingResponse fromSupplierInfo(OrderRating orderRating) {
        OrderRatingResponse orderRatingResponse = OrderRatingResponse.builder()
                .id(orderRating.getId())
                .ratingStar(orderRating.getRatingStar())
                .responseMessage(orderRating.getResponseMessage())
                .users(orderRating.getUsers())
                .build();
        return orderRatingResponse;
    }
}
