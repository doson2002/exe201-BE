package com.exe201.exe201be.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "food_item")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodItem extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private SupplierInfo supplierInfo;

    @Column(name = "description")
    private String description;

    @Column(name = "food_name")
    private String foodName;

    @Column(name = "inventory_quantity")
    private int inventoryQuantity;

    @Column(name = "quantity_sold")
    private int quantitySold;

    @Column(name = "price")
    private double price;

    @Column(name = "status")// ('available', 'pending', 'collected')
    private String status;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "ready_time")
    private Date readyTime = new Date();

    @Column(name = "category")
    private String category; //category("fee required","free")

    @Column(name = "is_offered")
    private int isOffered = 0;

    @OneToMany(mappedBy = "foodItem", cascade = CascadeType.ALL)
    private List<FoodItemType> foodItemTypes;
}
