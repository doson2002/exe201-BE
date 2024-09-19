package com.exe201.exe201be.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "supplier_info")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "restaurant_name", length = 200)
    private String restaurantName;

    @Column(name = "description", columnDefinition = "TEXT")
    private  String description;

    @Column(name = "address")
    private String address;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "slogan")
    private String slogan;




}
