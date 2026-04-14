package com.freelancedetector.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ProjectDTO {
    private Long projectId;
    private Long clientId;
    private Long userId;
    private String title;
    private String description;
    private String projectType;
    private Double agreedAmount;
    private Double agreedHours;
    private LocalDate deadline;
    private String status;
}
