package com.freelancedetector.service;

import com.freelancedetector.dto.RiskAssessmentDTO;
import com.freelancedetector.entity.BenchmarkRate;
import com.freelancedetector.entity.Payment;
import com.freelancedetector.entity.Project;
import com.freelancedetector.entity.RiskAssessment;
import com.freelancedetector.repository.BenchmarkRateRepository;
import com.freelancedetector.repository.PaymentRepository;
import com.freelancedetector.repository.ProjectRepository;
import com.freelancedetector.repository.RiskAssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AnalysisService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private WorkLogService workLogService;
    @Autowired
    private BenchmarkRateRepository benchmarkRateRepository;
    @Autowired
    private RiskAssessmentRepository riskAssessmentRepository;

    public RiskAssessmentDTO analyzeProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Double totalReceived = paymentService.getTotalReceived(projectId);
        Double totalHoursWorked = workLogService.getTotalHoursWorked(projectId);
        Integer totalRevisions = workLogService.getTotalRevisions(projectId);

        // Calculate Actual Hourly Rate (a)
        double actualHourlyRate = (totalHoursWorked != null && totalHoursWorked > 0) ? (totalReceived / totalHoursWorked) : 0.0;

        // Calculate Underpayment Score (b)
        BenchmarkRate benchmark = benchmarkRateRepository.findByRoleIgnoreCase(project.getProjectType())
                .orElse(new BenchmarkRate(null, project.getProjectType(), 20.0)); // Default to 20 if role not found
        double benchmarkRate = benchmark.getAverageHourlyRate();
        
        double underpaymentScore = 0.0;
        if (actualHourlyRate < benchmarkRate && actualHourlyRate > 0) {
            // How far below the benchmark are they?
            underpaymentScore = ((benchmarkRate - actualHourlyRate) / benchmarkRate) * 10.0;
        } else if (actualHourlyRate == 0.0 && totalHoursWorked > 0) {
            underpaymentScore = 10.0; // 0 pay = max exploitation
        }
        underpaymentScore = Math.min(10.0, underpaymentScore);

        // Calculate Delay Score (c)
        List<Payment> payments = paymentRepository.findByProjectProjectId(projectId);
        double totalDelayDays = 0;
        for (Payment p : payments) {
            LocalDate comparisonDate = p.getPaymentDate() != null ? p.getPaymentDate() : LocalDate.now();
            if (p.getDueDate() != null && comparisonDate.isAfter(p.getDueDate())) {
                totalDelayDays += ChronoUnit.DAYS.between(p.getDueDate(), comparisonDate);
            }
        }
        // Assuming 3 days average delay equals 1 risk point
        double delayScore = Math.min(10.0, totalDelayDays / 3.0);

        // Calculate Revision Score (d)
        double agreedHours = (project.getAgreedHours() != null && project.getAgreedHours() > 0) ? project.getAgreedHours() : 1.0;
        // The more revisions per agreed hour, the higher the score
        double revisionScore = Math.min(10.0, ((double) totalRevisions / agreedHours) * 10.0);

        // Calculate Unpaid Hours Score (e)
        double unpaidHoursCost = Math.max(0, (totalHoursWorked - agreedHours)) * actualHourlyRate;
        double unpaidHoursScore = Math.min(10.0, unpaidHoursCost / 50.0); // 1 point per $50 lost

        // Calculate Total Risk Score (f)
        double totalRiskScore = (underpaymentScore * 0.4) + (delayScore * 0.3) + (revisionScore * 0.15) + (unpaidHoursScore * 0.15);

        // Calculate Risk Level (g)
        String riskLevel = "LOW";
        if (totalRiskScore > 7) riskLevel = "CRITICAL";
        else if (totalRiskScore > 5) riskLevel = "HIGH";
        else if (totalRiskScore > 3) riskLevel = "MEDIUM";

        // Save or update RiskAssessment
        RiskAssessment assessment = riskAssessmentRepository.findByProjectProjectId(projectId)
                .orElse(new RiskAssessment());
        
        assessment.setProject(project);
        assessment.setUnderpaymentScore(underpaymentScore);
        assessment.setDelayScore(delayScore);
        assessment.setRevisionScore(revisionScore);
        assessment.setUnpaidHoursScore(unpaidHoursScore);
        assessment.setTotalRiskScore(totalRiskScore);
        assessment.setRiskLevel(riskLevel);

        RiskAssessment savedAssessment = riskAssessmentRepository.save(assessment);
        return convertToDTO(savedAssessment);
    }

    private RiskAssessmentDTO convertToDTO(RiskAssessment assessment) {
        RiskAssessmentDTO dto = new RiskAssessmentDTO();
        dto.setRiskId(assessment.getRiskId());
        dto.setProjectId(assessment.getProject() != null ? assessment.getProject().getProjectId() : null);
        dto.setDelayScore(assessment.getDelayScore());
        dto.setUnderpaymentScore(assessment.getUnderpaymentScore());
        dto.setRevisionScore(assessment.getRevisionScore());
        dto.setUnpaidHoursScore(assessment.getUnpaidHoursScore());
        dto.setTotalRiskScore(assessment.getTotalRiskScore());
        dto.setRiskLevel(assessment.getRiskLevel());
        return dto;
    }
}
