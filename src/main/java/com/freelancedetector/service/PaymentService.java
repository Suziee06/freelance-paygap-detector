package com.freelancedetector.service;

import com.freelancedetector.dto.PaymentDTO;
import com.freelancedetector.entity.Payment;
import com.freelancedetector.entity.Project;
import com.freelancedetector.repository.PaymentRepository;
import com.freelancedetector.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public PaymentDTO addPayment(PaymentDTO dto) {
        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Payment payment = new Payment();
        payment.setProject(project);
        payment.setAmountReceived(dto.getAmountReceived());
        payment.setPaymentDate(dto.getPaymentDate());
        payment.setDueDate(dto.getDueDate());
        payment.setPaymentStatus(dto.getPaymentStatus());

        Payment savedPayment = paymentRepository.save(payment);
        return convertToDTO(savedPayment);
    }

    public List<PaymentDTO> getPaymentsByProject(Long projectId) {
        return paymentRepository.findByProjectProjectId(projectId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Double getTotalReceived(Long projectId) {
        List<Payment> payments = paymentRepository.findByProjectProjectId(projectId);
        return payments.stream()
                .filter(p -> p.getAmountReceived() != null)
                .mapToDouble(Payment::getAmountReceived)
                .sum();
    }

    private PaymentDTO convertToDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentId(payment.getPaymentId());
        dto.setProjectId(payment.getProject() != null ? payment.getProject().getProjectId() : null);
        dto.setAmountReceived(payment.getAmountReceived());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setDueDate(payment.getDueDate());
        dto.setPaymentStatus(payment.getPaymentStatus());
        return dto;
    }
}
