package com.exe201.exe201be.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "food_order")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "order_time")
    private Date orderTime;

    @ManyToOne
    @JoinColumn(name = "supplier_info_id")
    private SupplierInfo supplierInfo;

    @Column(name = "pickup_time")
    private Date pickupTime;

    @Column(name = "pickup_location")
    private String pickupLocation;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "total_items")
    private int totalItems;

    @Column(name = "status")
    private String status;  //('pending', 'confirmed', 'completed')

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "payment_status")
    private int paymentStatus;


}
