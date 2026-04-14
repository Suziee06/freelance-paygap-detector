package com.freelancedetector.repository;

import com.freelancedetector.entity.BenchmarkRate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BenchmarkRateRepository extends JpaRepository<BenchmarkRate, Long> {
    Optional<BenchmarkRate> findByRoleIgnoreCase(String role);
    List<BenchmarkRate> findByAverageHourlyRateGreaterThan(Double rate);
    List<BenchmarkRate> findAllByOrderByAverageHourlyRateDesc();
}
