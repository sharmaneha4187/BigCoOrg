package com.bigco.org.neha;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class EmployeeCsvReader {
    /** Header: id,name,managerId,salary */
    public static Map<String, Employee> read(Path csvPath) throws IOException {
        Map<String, Employee> byId = new HashMap<>();
        try (BufferedReader br = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            String line; boolean first = true;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                if (first) { first = false; continue; }
                String[] p = line.split(",", -1);
                if (p.length < 4) { System.err.println("WARN bad line: " + line); continue; }
                String id = p[0].trim(), firstName = p[1].trim(), lastName = p[2].trim(), managerId = p[4].trim();
                double salary = Double.parseDouble(p[3].trim());
                if (id.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) { 
                	System.err.println("WARN missing fields: " + line); continue; 
                }
                 Employee e = new Employee(id, firstName, lastName, salary, managerId);
                byId.put(id, e);
            }
        }
        for (Employee e : byId.values()) {
            String mgr = e.getManagerId();
            if (mgr != null) {
                Employee m = byId.get(mgr);
                if (m != null) m.getDirectReports().add(e.getId());
                else System.err.println("WARN missing managerId '" + mgr + "' for employee " + e.getId());
            }
        }
        return byId;
    }
}
