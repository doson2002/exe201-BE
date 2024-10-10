package com.exe201.exe201be.controllers;


import com.exe201.exe201be.dtos.ChangePasswordDTO;
import com.exe201.exe201be.dtos.OtpDTO;
import com.exe201.exe201be.exceptions.DataNotFoundException;
import com.exe201.exe201be.repositories.UserRepository;
import com.exe201.exe201be.services.ForgotPasswordService;
import com.exe201.exe201be.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@RestController
@RequestMapping("/api/v1/forgot_password")
@RequiredArgsConstructor
public class ForgotPasswordController {
    private UserRepository userRepository;
    private final ForgotPasswordService forgotPasswordService;
    private final PasswordEncoder passwordEncoder;
    private final IUserService userService;

    @PostMapping("/send_otp/{email}")
    public ResponseEntity<?> sendOtp(@PathVariable String email) {
        Map<String, String> response = new HashMap<>();
        try {
            forgotPasswordService.verifyEmailAndSendOTP(email);
            response.put("status", "success");
            response.put("message", "Email sent for verification !!!");
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException ex) {
            response.put("status", "failed");
            response.put("errorMessage", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception ex) {
            response.put("status", "failed");
            response.put("errorMessage", "An unexpected error occurred.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/verify_otp/{email}")
    public ResponseEntity<?> verifyOtp(@PathVariable String email, @RequestParam("otp") Integer otp) {
        Map<String, String> response = new HashMap<>();
        try {
            forgotPasswordService.verifyOTP(email, otp);
            response.put("status", "success");
            response.put("message", "OTP verified successfully!");
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException ex) {
            response.put("status", "failed");
            response.put("errorMessage", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (RuntimeException ex) {
            response.put("status", "failed");
            response.put("errorMessage", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception ex) {
            response.put("status", "failed");
            response.put("errorMessage", "An unexpected error occurred.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/change_password/{email}")
    public ResponseEntity<Map<String, String>> changePasswordHandler(
            @RequestBody ChangePasswordDTO changePasswordDTO,
            @PathVariable String email) {

        Map<String, String> response = new HashMap<>();

        try {
            // Kiểm tra nếu password và retypePassword không giống nhau
            if (!Objects.equals(changePasswordDTO.password(), changePasswordDTO.retypePassword())) {
                response.put("status", "failed");
                response.put("errorMessage", "Passwords do not match. Please enter password again!");
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
            }

            // Mã hóa password
            String encodedPassword = passwordEncoder.encode(changePasswordDTO.password());

            // Cập nhật password của user
            userService.updatePassword(email, encodedPassword);

            // Phản hồi thành công
            response.put("status", "success");
            response.put("message", "Password has been changed!");
            return ResponseEntity.ok(response);

        } catch (DataNotFoundException ex) {
            // Trường hợp không tìm thấy người dùng
            response.put("status", "failed");
            response.put("errorMessage", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception ex) {
            // Trường hợp lỗi không mong đợi
            response.put("status", "failed");
            response.put("errorMessage", "An unexpected error occurred.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}


