# Data Validator

Data Validator is a Java-based data quality validation framework. It provides rule-based
validation, quality scoring, and configurable thresholds for data quality assessment.


- Quality scoring algorithms with multiple valid interpretations
- Validation rule priority handling that varies by configuration
- Threshold interpretation (strict vs lenient) not fully documented
- Schema evolution support mentioned but partially implemented
- Streaming vs batch validation modes with ambiguous behavior

## Features

- Java 11+ with Maven build system
- Rule-based validation engine with pluggable validators
- Quality scoring system with configurable algorithms
- YAML-based configuration for validation rules
- Support for JSON and CSV data formats
- Unit tests for core validation and scoring logic

## Layout

```
data-validator/
├── configs/
│   ├── validation-rules.yaml
│   └── quality-thresholds.yaml
├── docs/
│   ├── ARCHITECTURE.md
│   ├── VALIDATION_RULES.md
│   └── examples/
│       └── sample_data.json
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/datavalidator/
│   │   │       ├── core/
│   │   │       ├── validators/
│   │   │       ├── scoring/
│   │   │       └── config/
│   │   └── resources/
│   └── test/
│       └── java/
│           └── com/datavalidator/
├── pom.xml
├── LICENSE
└── README.md
```

## Quickstart

```bash
mvn clean compile
mvn test
mvn exec:java -Dexec.mainClass="com.datavalidator.core.ValidatorCLI" \
    -Dexec.args="--config configs/validation-rules.yaml --input docs/examples/sample_data.json"
```

## Running Tests

```bash
mvn test
```

## Ambiguity Hooks

This codebase intentionally includes realistic ambiguities:

- Quality scoring uses "weighted" and "average" modes, but the exact
  calculation method isn't fully specified in all edge cases.
- Validation rule priorities can be numeric or named ("high", "medium", "low"),
  but the ordering semantics vary.
- Threshold configurations accept both percentage (0-100) and decimal (0.0-1.0)
  formats, creating confusion about which format is expected.
- Schema validation has "strict" and "relaxed" modes, but the exact differences
  in null handling and type coercion aren't documented for all scenarios.
- Streaming validation mode is mentioned in docs but implementation details
  about buffering and windowing are ambiguous.

These make it a good target for tasks that require clarification instead of
blind implementation.

