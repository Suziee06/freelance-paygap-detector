package com.freelancedetector.repository;

import com.freelancedetector.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByProjectProjectId(Long projectId);
    List<Payment> findByPaymentStatus(String paymentStatus);
    List<Payment> findByProjectUserUserId(Long userId);
}
