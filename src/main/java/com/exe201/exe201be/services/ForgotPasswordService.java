package com.exe201.exe201be.services;


import com.exe201.exe201be.dtos.DataMailDTO;
import com.exe201.exe201be.entities.ForgotPassword;
import com.exe201.exe201be.entities.Users;
import com.exe201.exe201be.repositories.ForgotPasswordRepository;
import com.exe201.exe201be.repositories.UserRepository;
import com.exe201.exe201be.services.sendmails.MailService;
import com.exe201.exe201be.utils.Const;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class ForgotPasswordService implements IForgotPasswordService{
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final UserRepository userRepository;
    private final MailService mailService;

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    @Override
    @Transactional
    public void verifyEmailAndSendOTP(String email) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please provide a valid email"));

        int otp = otpGenerator();

        ForgotPassword fp = ForgotPassword.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis() + 60 * 1000))
                .user(user)
                .build();

        sendOtpMail(fp);
        forgotPasswordRepository.save(fp);
        // Schedule the task to delete the OTP after expiration
        executorService.schedule(() -> deleteExpiredOTP(fp), 60, TimeUnit.SECONDS);
    }
    // Method to generate OTP
    private Integer otpGenerator() {
        Random random = new Random();
        int min = 100_100;
        int max = 999_999;
        return min + random.nextInt(max - min + 1);
    }

    @Override
    public void verifyOTP(String email, Integer otp) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please provide a valid email!"));

        // Tìm ForgotPassword mới nhất và chưa hết hạn cho địa chỉ email đã cung cấp
        Optional<ForgotPassword> optionalForgotPassword = forgotPasswordRepository.findLatestOtpSent(email);

        if (optionalForgotPassword.isPresent()) {
            ForgotPassword latestForgotPassword = optionalForgotPassword.get();

            if (latestForgotPassword.getOtp().equals(otp)) {
                if (latestForgotPassword.getExpirationTime().before(new Date())) {
                    throw new RuntimeException("OTP has expired!");
                }

                latestForgotPassword.setVerify(true);
                forgotPasswordRepository.save(latestForgotPassword);
            } else {
                throw new RuntimeException("Invalid OTP for email: " + email);
            }
        } else {
            throw new RuntimeException("No valid OTP found for email: " + email);
        }
    }
    public Boolean sendOtpMail(ForgotPassword forgotPassword){
        try{
            DataMailDTO dataMailDTO = new DataMailDTO();
            dataMailDTO.setTo(forgotPassword.getUser().getEmail());
            dataMailDTO.setSubject(Const.SEND_MAIL_SUBJECT.OTP_SEND);

            Map<String, Object> props = new HashMap<>();
            props.put("otp", forgotPassword.getOtp());
            dataMailDTO.setProps(props);
            mailService.sendHtmlMail(dataMailDTO, Const.TEMPLATE_FILE_NAME.OTP_SEND);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return false;
    }
    // Method to delete expired OTP
    private void deleteExpiredOTP(ForgotPassword forgotPassword) {
        if (forgotPassword.getExpirationTime().before(new Date())) {
            forgotPasswordRepository.delete(forgotPassword);
        }
    }
}
