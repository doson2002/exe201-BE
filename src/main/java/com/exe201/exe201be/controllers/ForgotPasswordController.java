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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RestController
@RequestMapping("/api/v1/forgot_password")
@RequiredArgsConstructor
public class ForgotPasswordController {
    private UserRepository userRepository;
    private final ForgotPasswordService forgotPasswordService;
    private final PasswordEncoder passwordEncoder;
    private final IUserService userService;

    @PostMapping("/verify_mail/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email) {
        forgotPasswordService.verifyEmailAndSendOTP(email);// Check valid email and send otp
        return ResponseEntity.ok("Email sent for verification !!!");

    }
    @PostMapping("/verify_otp/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable String email,
                                            @RequestBody OtpDTO otpDTO) {
        forgotPasswordService.verifyOTP(email, otpDTO.otp());
        return ResponseEntity.ok("OTP verified!");
    }
    @PostMapping("/change_password/{email}")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePasswordDTO changePasswordDTO,
                                                        @PathVariable String email) throws DataNotFoundException {
        if (!Objects.equals(changePasswordDTO.password(), changePasswordDTO.retypePassword())) {
            return new ResponseEntity<>("Please enter password again!", HttpStatus.EXPECTATION_FAILED);
        }

        String encodedPassword = passwordEncoder.encode(changePasswordDTO.password());
        userService.updatePassword(email, encodedPassword);

        //forgotPasswordService.deleteForgotPassword(fp.getId());
        return ResponseEntity.ok("Password has been changed!");
    }
}


