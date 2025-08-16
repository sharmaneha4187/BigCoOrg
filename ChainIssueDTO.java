package com.bigco.org.neha.dto;

public record ChainIssueDTO(
        String employeeId,
        String employeeName,
        int managersBetween,
        int excess
) {}
