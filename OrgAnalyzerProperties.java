package com.bigco.org.neha.config;

import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "org")
public class OrgAnalyzerProperties {

    private double minMultiplier = 1.2;
    private double maxMultiplier = 1.5;
    @Positive
    private int maxManagersBetween = 4;

    public double getMinMultiplier() { return minMultiplier; }
    public void setMinMultiplier(double minMultiplier) { this.minMultiplier = minMultiplier; }

    public double getMaxMultiplier() { return maxMultiplier; }
    public void setMaxMultiplier(double maxMultiplier) { this.maxMultiplier = maxMultiplier; }

    public int getMaxManagersBetween() { return maxManagersBetween; }
    public void setMaxManagersBetween(int maxManagersBetween) { this.maxManagersBetween = maxManagersBetween; }

}
