package com.freelancedetector.controller;

import com.freelancedetector.dto.WorkLogDTO;
import com.freelancedetector.service.WorkLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/worklogs")
@CrossOrigin(origins = "*")
public class WorkLogController {

    @Autowired
    private WorkLogService workLogService;

    @PostMapping
    public ResponseEntity<?> addWorkLog(@RequestBody WorkLogDTO workLogDTO) {
        try {
            WorkLogDTO savedLog = workLogService.addWorkLog(workLogDTO);
            return new ResponseEntity<>(savedLog, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getLogsByProject(@PathVariable Long projectId) {
        try {
            List<WorkLogDTO> logs = workLogService.getLogsByProject(projectId);
            return new ResponseEntity<>(logs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/project/{projectId}/totals")
    public ResponseEntity<?> getProjectTotals(@PathVariable Long projectId) {
        try {
            Double totalHours = workLogService.getTotalHoursWorked(projectId);
            Integer totalRevisions = workLogService.getTotalRevisions(projectId);
            return new ResponseEntity<>(Map.of(
                    "totalHoursWorked", totalHours,
                    "totalRevisions", totalRevisions
            ), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
