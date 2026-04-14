package com.freelancedetector.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class WorkLogDTO {
    private Long logId;
    private Long projectId;
    private LocalDate workDate;
    private Double hoursWorked;
    private Integer revisionsDone;
    private String notes;
}
