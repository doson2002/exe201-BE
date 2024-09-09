package com.exe201.exe201be.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "forgot_password")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ForgotPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "otp", nullable = false)
    private Integer otp;
    @Column(name = "expiration_time", nullable = false)
    private Date expirationTime;

    @Column(name = "verified")
    private boolean verify;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;
}