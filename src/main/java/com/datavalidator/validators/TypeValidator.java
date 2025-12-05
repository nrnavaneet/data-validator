package com.datavalidator.validators;

import com.datavalidator.core.ValidationResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Validates field types and formats.
 */
public class TypeValidator implements Validator {
    private final String name;
    private final Map<String, String> fieldTypes; // Field name -> expected type
    private final int priority;
    private final boolean allowCoercion; // Coercion behavior not fully specified

    public TypeValidator(String name, Map<String, String> fieldTypes, int priority, boolean allowCoercion) {
        this.name = name;
        this.fieldTypes = fieldTypes;
        this.priority = priority;
        this.allowCoercion = allowCoercion;
    }

    @Override
    public ValidationResult validate(Map<String, Object> record) {
        List<ValidationResult.ValidationError> errors = new ArrayList<>();
        double score = 1.0;
        int checkedFields = 0;

        for (Map.Entry<String, String> entry : fieldTypes.entrySet()) {
            String field = entry.getKey();
            String expectedType = entry.getValue();
            
            if (!record.containsKey(field)) {
                continue; // Missing fields handled by RequiredFieldValidator
            }

            checkedFields++;
            Object value = record.get(field);
            
            if (value == null) {
                continue; // Null handling ambiguous - depends on nullable config
            }

            boolean typeMatches = checkType(value, expectedType, allowCoercion);
            if (!typeMatches) {
                ValidationResult.Severity severity = allowCoercion 
                    ? ValidationResult.Severity.LOW 
                    : ValidationResult.Severity.HIGH;
                errors.add(new ValidationResult.ValidationError(
                    name, field, 
                    String.format("Expected type %s but got %s", expectedType, value.getClass().getSimpleName()),
                    severity
                ));
                score -= 0.3; // Penalty calculation method varies
            }
        }

        if (checkedFields > 0) {
            score = Math.max(0.0, score / checkedFields * fieldTypes.size());
        }

        boolean valid = errors.isEmpty() || (allowCoercion && errors.stream()
            .noneMatch(e -> e.getSeverity() == ValidationResult.Severity.HIGH));
        
        return new ValidationResult(valid, errors, Math.max(0.0, Math.min(1.0, score)));
    }

    private boolean checkType(Object value, String expectedType, boolean allowCoercion) {
        String actualType = value.getClass().getSimpleName().toLowerCase();
        String expected = expectedType.toLowerCase();

        if (actualType.contains(expected) || expected.contains(actualType)) {
            return true;
        }

        if (allowCoercion) {
            // Coercion rules not fully documented
            if (expected.equals("integer") && (value instanceof Number || value instanceof String)) {
                try {
                    Integer.parseInt(value.toString());
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            if (expected.equals("double") && (value instanceof Number || value instanceof String)) {
                try {
                    Double.parseDouble(value.toString());
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }

        return false;
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

