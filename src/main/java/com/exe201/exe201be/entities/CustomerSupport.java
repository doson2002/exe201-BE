package com.exe201.exe201be.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "customer_support")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerSupport extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "issue_description")
    private String issueDescription;

    @Column(name = "status")
    private String status;

}
