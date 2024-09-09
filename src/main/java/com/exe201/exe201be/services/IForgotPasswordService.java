package com.exe201.exe201be.services;

public interface IForgotPasswordService {
    void verifyEmailAndSendOTP(String email);
    void verifyOTP(String email, Integer otp);
}
