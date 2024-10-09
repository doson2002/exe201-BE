package com.exe201.exe201be.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.Date;

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
    private String restaurantName = "";

    @Column(name = "description", columnDefinition = "TEXT")
    private  String description = "";

    @Column(name = "address")
    private String address = "";

    @Column(name = "img_url")
    private String imgUrl = "";

    @Column(name = "total_star_rating")
    private double totalStarRating = 0;

    @Column(name = "total_review_count")
    private int totalReviewCount =0;

    @Column(name = "cash")
    private double cash = 0;

    @ManyToOne
    @JoinColumn(name = "supplier_type_id")
    private SupplierType supplierType;

    // Thêm thời gian mở cửa và đóng cửa
    @Column(name = "open_time")
    private LocalTime openTime = LocalTime.of(0, 0, 0); // Ví dụ: Mở cửa lúc 08:00:00

    @Column(name = "close_time")
    private LocalTime closeTime = LocalTime.of(0, 0, 0); // Đóng cửa lúc 00:00:00

    // Thêm biến để lưu tọa độ
    @Column(name = "latitude")
    private double latitude = 0;

    @Column(name = "longitude")
    private double longitude = 0;
}
