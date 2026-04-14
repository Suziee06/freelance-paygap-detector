package com.freelancedetector.repository;

import com.freelancedetector.entity.WorkLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {
    List<WorkLog> findByProjectProjectId(Long projectId);
    List<WorkLog> findByRevisionsDoneGreaterThan(Integer count);
    List<WorkLog> findByProjectUserUserId(Long userId);
}
