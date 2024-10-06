package com.exe201.exe201be.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "FAQ")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FAQ extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title = "";

    @Column(name = "article", columnDefinition = "TEXT")
    private String article = "";
}
