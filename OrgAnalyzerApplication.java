package com.bigco.org.neha;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.bigco.org.neha.service.OrgAnalysisService;

import java.nio.file.Path;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Org Analyzer API", version = "1.1.0", description = "Analyze org CSV to find salary-rule violations and long reporting chains."))
public class OrgAnalyzerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrgAnalyzerApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(OrgAnalysisService service) {
        return args -> {
            if (args.length == 0) return;
            Path csv = Path.of(args[0]);
            OrgAnalysisReport r = service.analyze(csv);
            System.out.println("=== Org Analyzer Report ===");
            System.out.println("CSV: " + csv.toAbsolutePath());
            System.out.println("1) Managers earning LESS than required:");
            r.underpaid().forEach(v -> System.out.printf(
                    " - %s  (%s): salary=%.2f, avgSub=%.2f, minAllowed=%.2f => under by %.2f%n",
                    v.managerFirstName()+" "+v.managerLastName(), v.managerId(), v.salary(), v.avgSub(), v.minAllowed(), v.delta()));
            System.out.println("2) Managers earning MORE than allowed:");
            r.overpaid().forEach(v -> System.out.printf(
                    " - %s  (%s): salary=%.2f, avgSub=%.2f, maxAllowed=%.2f => over by %.2f%n",
                    v.managerFirstName()+" "+v.managerLastName(), v.managerId(), v.salary(), v.avgSub(), v.maxAllowed(), v.delta()));
            System.out.println("3) Employees with too long reporting line (> configured max):");
            r.longChains().forEach(c -> System.out.printf(
                    " - %s (%s): managersBetween=%d => too long by %d%n",
                    c.employeeFirstName()+" "+ c.employeeLastName(), c.employeeId(), c.managersBetween(), c.excess()));
        };
    }
}
