package com.exe201.exe201be.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "supplier_type")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplierType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_name", length = 100, nullable = false)
    private String typeName;

    @Column(name = "img_url", columnDefinition = "TEXT")
    private String imgUrl;
}