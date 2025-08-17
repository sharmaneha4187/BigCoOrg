package com.bigco.org.neha.service;

import com.bigco.org.neha.Employee;
import com.bigco.org.neha.EmployeeCsvReader;
import com.bigco.org.neha.OrgAnalysisReport;
import com.bigco.org.neha.OrgAnalyzer;
import com.bigco.org.neha.dto.ChainIssueDTO;
import com.bigco.org.neha.dto.SalaryViolationDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrgAnalysisService {

    private final OrgAnalyzer analyzer;

    public OrgAnalysisService(OrgAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    public OrgAnalysisReport analyze(Path csvPath) throws IOException {
        Map<String, Employee> byId = EmployeeCsvReader.read(csvPath);
        Optional<Employee> ceoOpt = OrgAnalyzer.findCEO(byId);
        Employee ceo = ceoOpt.orElseThrow(() -> new IllegalArgumentException("CEO not found"));

        var underpaid = analyzer.findUnderpaidManagers(byId).stream().map(v ->
                new SalaryViolationDTO(
                        v.manager.getId(),
                        v.manager.getFirstName(),
                        v.manager.getLastName(),
                        v.manager.getSalary(),
                        v.avgSub,
                        v.minAllowed,
                        v.maxAllowed,
                        v.delta,
                        v.type.name()
                )).collect(Collectors.toList());

        var overpaid = analyzer.findOverpaidManagers(byId).stream().map(v ->
                new SalaryViolationDTO(
                        v.manager.getId(),
                        v.manager.getFirstName(),
                        v.manager.getLastName(),
                        v.manager.getSalary(),
                        v.avgSub,
                        v.minAllowed,
                        v.maxAllowed,
                        v.delta,
                        v.type.name()
                )).collect(Collectors.toList());

        var longChains = analyzer.findTooLongChains(byId, ceo).stream().map(c ->
                new com.bigco.org.neha.dto.ChainIssueDTO(
                        c.employee.getId(),
                        c.employee.getFirstName(),
                        c.employee.getLastName(),
                        c.managersBetween,
                        c.excess
                )).collect(Collectors.toList());

        return new OrgAnalysisReport(underpaid, overpaid, longChains);
 
    }
}
