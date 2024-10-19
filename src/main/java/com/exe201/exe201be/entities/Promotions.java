package com.exe201.exe201be.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Builder
@Table(name = "promotions")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Promotions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;

    @Column(name = "discount_percentage")
    private double discountPercentage = 0;

    @Column(name = "fixed_discount_amount")
    private double fixedDiscountAmount = 0;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description = "";

    @Column(name = "status")
    private boolean status;

    @Column(name = "promotion_type")
    private int promotionType;      //1 là system , 2 là partner

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private SupplierInfo supplierInfo;

}