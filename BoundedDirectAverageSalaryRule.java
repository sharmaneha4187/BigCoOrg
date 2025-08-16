package com.bigco.org.neha.policy;

import java.util.*;

import com.bigco.org.neha.Employee;
import com.bigco.org.neha.OrgAnalyzer;

public class BoundedDirectAverageSalaryRule implements SalaryRule {
    private final double minMultiplier;
    private final double maxMultiplier;

    public BoundedDirectAverageSalaryRule(double minMultiplier, double maxMultiplier) {
        if (minMultiplier <= 0 || maxMultiplier <= 0 || minMultiplier >= maxMultiplier)
            throw new IllegalArgumentException("Bad multipliers");
        this.minMultiplier = minMultiplier;
        this.maxMultiplier = maxMultiplier;
    }

    @Override
    public List<OrgAnalyzer.SalaryViolation> evaluate(Map<String, Employee> byId) {
        List<OrgAnalyzer.SalaryViolation> out = new ArrayList<>();
        for (Employee m : byId.values()) {
            if (m.getDirectReports().isEmpty()) continue;
            double avg = m.getDirectReports().stream().map(byId::get).filter(Objects::nonNull).mapToDouble(Employee::getSalary).average().orElse(Double.NaN);
            if (Double.isNaN(avg)) continue;
            double minAllowed = minMultiplier * avg;
            double maxAllowed = maxMultiplier * avg;
            double sal = m.getSalary();
            if (sal < minAllowed) {
                out.add(new OrgAnalyzer.SalaryViolation(m, avg, minAllowed, maxAllowed, (minAllowed - sal), OrgAnalyzer.SalaryViolation.Type.UNDERPAID));
            } else if (sal > maxAllowed) {
                out.add(new OrgAnalyzer.SalaryViolation(m, avg, minAllowed, maxAllowed, (sal - maxAllowed), OrgAnalyzer.SalaryViolation.Type.OVERPAID));
            }
        }
        return out;
    }
}
