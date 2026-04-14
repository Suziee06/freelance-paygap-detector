package com.freelancedetector.dto;

import lombok.Data;

@Data
public class RiskAssessmentDTO {
    private Long riskId;
    private Long projectId;
    private Double delayScore;
    private Double underpaymentScore;
    private Double revisionScore;
    private Double unpaidHoursScore;
    private Double totalRiskScore;
    private String riskLevel;
}
