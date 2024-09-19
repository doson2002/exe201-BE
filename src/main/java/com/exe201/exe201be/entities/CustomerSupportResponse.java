package com.exe201.exe201be.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "customer_support_response")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerSupportResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_support_id")
    private CustomerSupport customerSupport;

    @Column(name = "response_message")
    private String responseMessage;

    @Column(name = "status")
    private String status;

    @Column(name = "responsed_at")
    private Date responsedAt;
}
