package com.bigco.org.neha.dto;

public record SalaryViolationDTO(
        String managerId,
        String managerName,
        double salary,
        double avgSub,
        double minAllowed,
        double maxAllowed,
        double delta,
        String type
) {}
