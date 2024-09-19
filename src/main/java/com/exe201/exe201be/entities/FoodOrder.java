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

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "order_time")
    private Date orderTime;

    @Column(name = "estimated_pickup_time")
    private Date estimatedPickupTime;

    @Column(name = "pickup_location")
    private String pickupLocation;

    @Column(name = "status")
    private String status;  //('pending', 'confirmed', 'completed')

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "payment_status")
    private int paymentStatus;


}
