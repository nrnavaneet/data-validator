package com.datavalidator.validators;

import com.datavalidator.core.ValidationResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Validates that required fields are present.
 */
public class RequiredFieldValidator implements Validator {
    private final String name;
    private final List<String> requiredFields;
    private final int priority;
    private final boolean strictMode; // Strict mode behavior not fully documented

    public RequiredFieldValidator(String name, List<String> requiredFields, int priority, boolean strictMode) {
        this.name = name;
        this.requiredFields = new ArrayList<>(requiredFields);
        this.priority = priority;
        this.strictMode = strictMode;
    }

    @Override
    public ValidationResult validate(Map<String, Object> record) {
        List<ValidationResult.ValidationError> errors = new ArrayList<>();
        double score = 1.0;

        for (String field : requiredFields) {
            if (!record.containsKey(field) || record.get(field) == null) {
                ValidationResult.Severity severity = strictMode 
                    ? ValidationResult.Severity.CRITICAL 
                    : ValidationResult.Severity.MEDIUM;
                errors.add(new ValidationResult.ValidationError(
                    name, field, "Required field is missing or null", severity
                ));
                // Score penalty - exact calculation method varies
                score -= 1.0 / requiredFields.size();
            } else if (record.get(field).toString().trim().isEmpty()) {
                // Empty string handling - ambiguous: is this an error?
                if (strictMode) {
                    errors.add(new ValidationResult.ValidationError(
                        name, field, "Required field is empty", ValidationResult.Severity.HIGH
                    ));
                    score -= 0.5 / requiredFields.size();
                }
            }
        }

        boolean valid = errors.isEmpty() || (!strictMode && errors.stream()
            .noneMatch(e -> e.getSeverity() == ValidationResult.Severity.CRITICAL));
        
        return new ValidationResult(valid, errors, Math.max(0.0, score));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPriority() {
        return priority;
    }
}

