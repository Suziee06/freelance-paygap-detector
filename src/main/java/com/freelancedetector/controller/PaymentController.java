package com.freelancedetector.controller;

import com.freelancedetector.dto.PaymentDTO;
import com.freelancedetector.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> addPayment(@RequestBody PaymentDTO paymentDTO) {
        try {
            PaymentDTO savedPayment = paymentService.addPayment(paymentDTO);
            return new ResponseEntity<>(savedPayment, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getPaymentsByProject(@PathVariable Long projectId) {
        try {
            List<PaymentDTO> payments = paymentService.getPaymentsByProject(projectId);
            return new ResponseEntity<>(payments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/project/{projectId}/total")
    public ResponseEntity<?> getTotalReceived(@PathVariable Long projectId) {
        try {
            Double total = paymentService.getTotalReceived(projectId);
            return new ResponseEntity<>(Map.of("totalReceived", total), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
