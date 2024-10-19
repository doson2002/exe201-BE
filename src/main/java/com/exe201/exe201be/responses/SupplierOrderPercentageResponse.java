package com.exe201.exe201be.responses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierOrderPercentageResponse {
    private Long supplierId;
    private Long orderCount;
    private int percentage;
    private String supplierName;  // Thêm trường supplierName
    private String imgUrl;        // Thêm trường imgUrl

}
