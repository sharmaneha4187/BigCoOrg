package com.bigco.org.neha.policy;

import java.util.*;

import com.bigco.org.neha.Employee;
import com.bigco.org.neha.OrgAnalyzer;

public class MaxManagersBetweenChainRule implements ChainRule {
    private final int maxManagersBetween;

    public MaxManagersBetweenChainRule(int maxManagersBetween) {
        if (maxManagersBetween < 0) throw new IllegalArgumentException("maxManagersBetween >= 0");
        this.maxManagersBetween = maxManagersBetween;
    }

    @Override
    public List<OrgAnalyzer.ChainTooLong> evaluate(Map<String, Employee> byId, Employee ceo) {
        List<OrgAnalyzer.ChainTooLong> out = new ArrayList<>();
        Map<String, Integer> memo = new HashMap<>();
        for (Employee e : byId.values()) {
            if (e.getId().equals(ceo.getId())) continue;
            int between = countManagersBetween(e.getId(), byId, ceo.getId(), memo, new HashSet<>());
            if (between == Integer.MIN_VALUE) continue;
            if (between > maxManagersBetween) {
                out.add(new OrgAnalyzer.ChainTooLong(e, between, between - maxManagersBetween));
            }
        }
        return out;
    }

    private static int countManagersBetween(String empId, Map<String, Employee> byId, String ceoId,
                                            Map<String, Integer> memo, Set<String> visiting) {
        if (empId.equals(ceoId)) return 0;
        if (memo.containsKey(empId)) return memo.get(empId);
        Employee e = byId.get(empId);
        if (e == null) return Integer.MIN_VALUE;
        String mgr = e.getManagerId();
        if (mgr == null) {
            System.err.println("WARN: Employee " + empId + " is top-level but not CEO; skipping long-chain check.");
            memo.put(empId, 0);
            return 0;
        }
        if (visiting.contains(empId)) {
            System.err.println("WARN: Cycle detected at " + empId);
            return Integer.MIN_VALUE;
        }
        visiting.add(empId);
        if (!byId.containsKey(mgr)) {
            System.err.println("WARN: Missing manager " + mgr + " for employee " + empId);
            visiting.remove(empId);
            return Integer.MIN_VALUE;
        }
        int up = countManagersBetween(mgr, byId, ceoId, memo, visiting);
        visiting.remove(empId);
        if (up == Integer.MIN_VALUE) return Integer.MIN_VALUE;
        int between = up + 1;
        memo.put(empId, between);
        return between;
    }
}
