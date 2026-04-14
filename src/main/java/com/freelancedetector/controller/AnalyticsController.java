package com.freelancedetector.controller;

import com.freelancedetector.service.GenderPayGapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    @Autowired
    private GenderPayGapService genderPayGapService;

    @GetMapping("/gender-gap")
    public ResponseEntity<?> getGenderGapStats(@RequestParam String projectType) {
        try {
            Map<String, Double> stats = genderPayGapService.getGenderGapStats(projectType);
            return new ResponseEntity<>(stats, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
