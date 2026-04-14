package com.freelancedetector.dto;

import lombok.Data;

@Data
public class ClientDTO {
    private Long clientId;
    private Long userId;
    private String clientName;
    private String email;
    private String industry;
    private String country;
}
