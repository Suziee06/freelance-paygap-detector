package com.freelancedetector.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PaymentDTO {
    private Long paymentId;
    private Long projectId;
    private Double amountReceived;
    private LocalDate paymentDate;
    private LocalDate dueDate;
    private String paymentStatus;
}
