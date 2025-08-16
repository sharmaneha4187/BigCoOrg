# Org Analyzer — Spring Boot

A REST API that analyzes an organization CSV and reports:
- managers under the 1.2× minimum vs avg of their **direct** reports,
- managers over the 1.5× maximum,
- employees with too-long chains (>4 managers between them and CEO).

Uses Strategy Pattern for pluggable rules.

## Build & Run
```bash
mvn -q clean package
java -jar target/org-analyzer-boot-1.0.0.jar
```

## REST API
### POST /api/v1/orgs/analyze  (multipart/form-data)
Form field `file` = CSV (header: `id,name,managerId,salary`).
Returns JSON with `underpaid`, `overpaid`, `longChains`.

```bash
curl -F file=@src/test/resources/sample.csv http://localhost:8080/api/v1/orgs/analyze
```

### CLI (optional)
You can also pass a CSV path to the jar to print a console report:
```bash
java -jar target/org-analyzer-boot-1.0.0.jar src/test/resources/sample.csv
```
