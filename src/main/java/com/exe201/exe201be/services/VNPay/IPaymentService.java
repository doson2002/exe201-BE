package com.exe201.exe201be.services.VNPay;

import com.exe201.exe201be.dtos.PaymentDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface IPaymentService {
    PaymentDTO.VNPayResponse createVnPayPayment(HttpServletRequest request);
}
