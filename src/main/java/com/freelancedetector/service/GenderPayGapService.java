package com.freelancedetector.service;

import com.freelancedetector.entity.Project;
import com.freelancedetector.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GenderPayGapService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private WorkLogService workLogService;

    public Map<String, Double> getGenderGapStats(String projectType) {
        // Fetch all projects, and filter by projectType (case-insensitive)
        List<Project> allProjects = projectRepository.findAll().stream()
                .filter(p -> p.getProjectType() != null && p.getProjectType().equalsIgnoreCase(projectType))
                .collect(Collectors.toList());

        // Track totals: Map<Gender, Double[0=totalRateSum, 1=count]>
        Map<String, double[]> genderAggregations = new HashMap<>();

        for (Project project : allProjects) {
            if (project.getUser() == null || project.getUser().getGender() == null) continue;
            String gender = project.getUser().getGender().toUpperCase();

            Double totalReceived = paymentService.getTotalReceived(project.getProjectId());
            Double totalHours = workLogService.getTotalHoursWorked(project.getProjectId());

            if (totalHours != null && totalHours > 0) {
                double hourlyRate = totalReceived / totalHours;

                // Add to aggregations
                genderAggregations.putIfAbsent(gender, new double[]{0.0, 0.0});
                genderAggregations.get(gender)[0] += hourlyRate; // sum of rates
                genderAggregations.get(gender)[1] += 1.0;        // count
            }
        }

        // Calculate final averages
        Map<String, Double> results = new HashMap<>();
        for (Map.Entry<String, double[]> entry : genderAggregations.entrySet()) {
            String gender = entry.getKey();
            double sumRates = entry.getValue()[0];
            double count = entry.getValue()[1];
            
            double averageRate = sumRates / count;
            
            // Round to 2 decimal places
            averageRate = Math.round(averageRate * 100.0) / 100.0;
            results.put(gender, averageRate);
        }

        return results;
    }
}
