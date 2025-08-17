package com.bigco.org.neha.dto;

public record SalaryViolationDTO(
        String managerId,
        String managerFirstName,
        String managerLastName,
        double salary,
        double avgSub,
        double minAllowed,
        double maxAllowed,
        double delta,
        String type
) {}
