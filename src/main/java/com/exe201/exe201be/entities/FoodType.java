package com.exe201.exe201be.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "food_type")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_name", length = 100, nullable = false)
    private String typeName;
}