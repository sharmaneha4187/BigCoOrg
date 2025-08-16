package com.bigco.org.neha;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bigco.org.neha.policy.BoundedDirectAverageSalaryRule;
import com.bigco.org.neha.policy.ChainRule;
import com.bigco.org.neha.policy.MaxManagersBetweenChainRule;
import com.bigco.org.neha.policy.SalaryRule;

@Configuration
public class OrgAnalyzerConfig {

    @Bean
    public SalaryRule salaryRule() {
        return new BoundedDirectAverageSalaryRule(1.2, 1.5);
    }

    @Bean
    public ChainRule chainRule() {
        return new MaxManagersBetweenChainRule(4);
    }

    @Bean
    public OrgAnalyzer orgAnalyzer(SalaryRule salaryRule, ChainRule chainRule) {
        return new OrgAnalyzer(salaryRule, chainRule);
    }
}
