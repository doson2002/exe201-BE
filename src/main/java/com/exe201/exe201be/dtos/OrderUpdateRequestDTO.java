package com.exe201.exe201be.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
import java.util.List;
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateRequestDTO {

    private String status;  // Trạng thái đơn hàng: 'pending', 'confirmed', 'completed'

    @JsonProperty("estimated_pickup_time")
    private Date estimatedPickupTime;

    @JsonProperty("payment_method")
    private String paymentMethod;  // Phương thức thanh toán (ví dụ: 'cash', 'credit_card')

    @JsonProperty("pickup_location")
    private String pickupLocation;  // Địa chỉ nhận hàng mới (nếu thay đổi)

    @JsonProperty("payment_status")
    private Integer paymentStatus;  // Trạng thái thanh toán: 0 (chưa thanh toán), 1 (đã thanh toán)

//    // Danh sách các món hàng cần cập nhật (nếu có)
//    private List<OrderRequestDTO> items;
    @JsonProperty("user_id")
    private Long userId;


}
