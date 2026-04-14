package com.freelancedetector.dto;

import lombok.Data;

@Data
public class BenchmarkRateDTO {
    private Long id;
    private String role;
    private Double averageHourlyRate;
}
