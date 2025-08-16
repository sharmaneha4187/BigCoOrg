package com.bigco.org.neha.policy;

import java.util.List;
import java.util.Map;

import com.bigco.org.neha.Employee;
import com.bigco.org.neha.OrgAnalyzer;

public interface ChainRule {
    List<OrgAnalyzer.ChainTooLong> evaluate(Map<String, Employee> byId, Employee ceo);
}
