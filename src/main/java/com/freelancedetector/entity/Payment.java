package com.freelancedetector.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    private Double amountReceived;
    private LocalDate paymentDate;
    
    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false)
    private String paymentStatus;
}
