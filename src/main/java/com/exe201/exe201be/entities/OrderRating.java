package com.exe201.exe201be.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "order_rating")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRating extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rating_star")
    private int ratingStar;

    @Column(name = "response_message",columnDefinition = "TEXT")
    private String responseMessage;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private SupplierInfo supplierInfo;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;
}
