package com.freelancedetector.repository;

import com.freelancedetector.entity.RiskAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RiskAssessmentRepository extends JpaRepository<RiskAssessment, Long> {
    Optional<RiskAssessment> findByProjectProjectId(Long projectId);
    List<RiskAssessment> findByRiskLevel(String riskLevel);
    List<RiskAssessment> findByProjectUserUserId(Long userId);
}
