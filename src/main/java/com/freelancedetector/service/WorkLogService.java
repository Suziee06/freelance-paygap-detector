package com.freelancedetector.service;

import com.freelancedetector.dto.WorkLogDTO;
import com.freelancedetector.entity.Project;
import com.freelancedetector.entity.WorkLog;
import com.freelancedetector.repository.ProjectRepository;
import com.freelancedetector.repository.WorkLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkLogService {

    @Autowired
    private WorkLogRepository workLogRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public WorkLogDTO addWorkLog(WorkLogDTO dto) {
        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        WorkLog workLog = new WorkLog();
        workLog.setProject(project);
        workLog.setWorkDate(dto.getWorkDate());
        workLog.setHoursWorked(dto.getHoursWorked());
        workLog.setRevisionsDone(dto.getRevisionsDone());
        workLog.setNotes(dto.getNotes());

        WorkLog savedLog = workLogRepository.save(workLog);
        return convertToDTO(savedLog);
    }

    public List<WorkLogDTO> getLogsByProject(Long projectId) {
        return workLogRepository.findByProjectProjectId(projectId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Double getTotalHoursWorked(Long projectId) {
        List<WorkLog> logs = workLogRepository.findByProjectProjectId(projectId);
        return logs.stream()
                .filter(l -> l.getHoursWorked() != null)
                .mapToDouble(WorkLog::getHoursWorked)
                .sum();
    }

    public Integer getTotalRevisions(Long projectId) {
        List<WorkLog> logs = workLogRepository.findByProjectProjectId(projectId);
        return logs.stream()
                .filter(l -> l.getRevisionsDone() != null)
                .mapToInt(WorkLog::getRevisionsDone)
                .sum();
    }

    private WorkLogDTO convertToDTO(WorkLog workLog) {
        WorkLogDTO dto = new WorkLogDTO();
        dto.setLogId(workLog.getLogId());
        dto.setProjectId(workLog.getProject() != null ? workLog.getProject().getProjectId() : null);
        dto.setWorkDate(workLog.getWorkDate());
        dto.setHoursWorked(workLog.getHoursWorked());
        dto.setRevisionsDone(workLog.getRevisionsDone());
        dto.setNotes(workLog.getNotes());
        return dto;
    }
}
