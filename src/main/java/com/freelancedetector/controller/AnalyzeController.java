package com.freelancedetector.controller;

import com.freelancedetector.dto.RiskAssessmentDTO;
import com.freelancedetector.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/analyze")
@CrossOrigin(origins = "*")
public class AnalyzeController {

    @Autowired
    private AnalysisService analysisService;

    @PostMapping("/{projectId}")
    public ResponseEntity<?> analyzeProject(@PathVariable Long projectId) {
        try {
            RiskAssessmentDTO result = analysisService.analyzeProject(projectId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
