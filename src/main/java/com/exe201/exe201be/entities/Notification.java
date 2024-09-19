package com.exe201.exe201be.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "notification")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "notification_type")
    private String notificationType;

}
