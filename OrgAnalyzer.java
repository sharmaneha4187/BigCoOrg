package com.bigco.org.neha;

import java.util.*;
import java.util.stream.Collectors;

import com.bigco.org.neha.policy.BoundedDirectAverageSalaryRule;
import com.bigco.org.neha.policy.ChainRule;
import com.bigco.org.neha.policy.MaxManagersBetweenChainRule;
import com.bigco.org.neha.policy.SalaryRule;

public class OrgAnalyzer {

    public static class SalaryViolation {
        public enum Type { UNDERPAID, OVERPAID }
        public final Employee manager;
        public final double avgSub;
        public final double minAllowed;
        public final double maxAllowed;
        public final double delta;
        public final Type type;
        public SalaryViolation(Employee manager, double avgSub, double minAllowed, double maxAllowed, double delta, Type type) {
            this.manager = manager; this.avgSub = avgSub; this.minAllowed = minAllowed; this.maxAllowed = maxAllowed; this.delta = delta; this.type = type;
        }
    }

    public static class ChainTooLong {
        public final Employee employee;
        public final int managersBetween;
        public final int excess;
        public ChainTooLong(Employee employee, int managersBetween, int excess) {
            this.employee = employee; this.managersBetween = managersBetween; this.excess = excess;
        }
    }

    private final SalaryRule salaryStrategy;
    private final ChainRule chainStrategy;

    public OrgAnalyzer(SalaryRule salaryStrategy, ChainRule chainStrategy) {
        this.salaryStrategy = Objects.requireNonNull(salaryStrategy);
        this.chainStrategy = Objects.requireNonNull(chainStrategy);
    }
//
//    public static OrgAnalyzer withDefaults() {
//        return new OrgAnalyzer(new BoundedDirectAverageSalaryRule(1.2, 1.5),
//                               new MaxManagersBetweenChainRule(4));
//    }

    public static Optional<Employee> findCEO(Map<String, Employee> byId) {
        List<Employee> ceos = byId.values().stream().filter(e -> e.getManagerId() == null).collect(Collectors.toList());
        if (ceos.size() == 1) return Optional.of(ceos.get(0));
        if (ceos.isEmpty()) System.err.println("WARN: No CEO (blank managerId) found.");
        else System.err.println("WARN: Multiple top-level nodes; using first as CEO: " + ceos.get(0).getId());
        return ceos.isEmpty() ? Optional.empty() : Optional.of(ceos.get(0));
    }

    public List<SalaryViolation> findSalaryViolations(Map<String, Employee> byId) {
        List<SalaryViolation> out = new ArrayList<>(salaryStrategy.evaluate(byId));
        out.sort(Comparator
                .comparing((SalaryViolation v) -> v.type)
                .thenComparing((SalaryViolation v) -> -v.delta));
        return out;
    }

    public List<SalaryViolation> findUnderpaidManagers(Map<String, Employee> byId) {
        List<SalaryViolation> all = findSalaryViolations(byId);
        all.removeIf(v -> v.type != SalaryViolation.Type.UNDERPAID);
        return all;
    }

    public List<SalaryViolation> findOverpaidManagers(Map<String, Employee> byId) {
        List<SalaryViolation> all = findSalaryViolations(byId);
        all.removeIf(v -> v.type != SalaryViolation.Type.OVERPAID);
        return all;
    }

    public List<ChainTooLong> findTooLongChains(Map<String, Employee> byId, Employee ceo) {
        return new ArrayList<>(chainStrategy.evaluate(byId, ceo));
    }
}
