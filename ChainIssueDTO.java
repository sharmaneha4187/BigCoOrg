package com.bigco.org.neha.dto;

public record ChainIssueDTO(
        String employeeId,
        String employeeFirstName,
        String employeeLastName,
        int managersBetween,
        int excess
) {}
