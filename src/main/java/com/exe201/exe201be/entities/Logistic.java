package com.exe201.exe201be.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "logistic")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Logistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "food_order_id")
    private FoodOrder foodOrder;

    @ManyToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @Column(name = "delivery_time")
    private Date deliveryTime;
}
