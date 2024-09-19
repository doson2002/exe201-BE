package com.exe201.exe201be.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;

public abstract class PaymentDTO {
    @Builder
    @AllArgsConstructor // Tạo constructor với tất cả các tham số
    public static class VNPayResponse {
        public String code;
        public String message;
        public String paymentUrl;
    }
}
