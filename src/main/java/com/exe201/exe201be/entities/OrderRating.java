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

    @OneToOne
    @JoinColumn(name = "food_order_id")
    private FoodOrder foodOrder;

    @Column(name = "rating_star")
    private int ratingStar;

    @Column(name = "response_message",columnDefinition = "TEXT")
    private String responseMessage;
}
