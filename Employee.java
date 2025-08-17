package com.bigco.org.neha;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Employee {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final String managerId; 
    private final double salary;
    private final List<String> directReports = new ArrayList<>();

    public Employee(String id, String firstName,String lastName,double salary, String managerId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.managerId = (managerId == null || managerId.isBlank()) ? null : managerId;
        
    }

    public String getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public double getSalary() { return salary; }
    public String getManagerId() { return managerId; }
    
    public List<String> getDirectReports() { return directReports; }

}
