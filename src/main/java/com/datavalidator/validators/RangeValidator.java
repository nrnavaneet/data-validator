package com.datavalidator.validators;

import com.datavalidator.core.ValidationResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Validates numeric fields are within specified ranges.
 */
public class RangeValidator implements Validator {
    private final String name;
    private final String field;
    private final Double min;
    private final Double max;
    private final boolean inclusive; // Inclusive vs exclusive bounds - ambiguous default
    private final int priority;

    public RangeValidator(String name, String field, Double min, Double max, boolean inclusive, int priority) {
        this.name = name;
        this.field = field;
        this.min = min;
        this.max = max;
        this.inclusive = inclusive; // Default behavior not specified
        this.priority = priority;
    }

    @Override
    public ValidationResult validate(Map<String, Object> record) {
        List<ValidationResult.ValidationError> errors = new ArrayList<>();
        
        if (!record.containsKey(field)) {
            return new ValidationResult(true, errors, 1.0); // Missing handled elsewhere
        }

        Object value = record.get(field);
        if (value == null) {
            return new ValidationResult(true, errors, 1.0); // Null handling ambiguous
        }

        double numValue;
        try {
            if (value instanceof Number) {
                numValue = ((Number) value).doubleValue();
            } else {
                numValue = Double.parseDouble(value.toString());
            }
        } catch (NumberFormatException e) {
            errors.add(new ValidationResult.ValidationError(
                name, field, "Value is not a number", ValidationResult.Severity.HIGH
            ));
            return new ValidationResult(false, errors, 0.0);
        }

        boolean inRange = true;
        if (min != null) {
            inRange = inclusive ? numValue >= min : numValue > min;
            if (!inRange) {
                errors.add(new ValidationResult.ValidationError(
                    name, field, 
                    String.format("Value %.2f is below minimum %s%.2f", numValue, inclusive ? "(" : "[", min),
                    ValidationResult.Severity.MEDIUM
                ));
            }
        }
        if (max != null && inRange) {
            inRange = inclusive ? numValue <= max : numValue < max;
            if (!inRange) {
                errors.add(new ValidationResult.ValidationError(
                    name, field,
                    String.format("Value %.2f is above maximum %s%.2f", numValue, inclusive ? ")" : "]", max),
                    ValidationResult.Severity.MEDIUM
                ));
            }
        }

        double score = inRange ? 1.0 : 0.5; // Score calculation method varies
        return new ValidationResult(inRange, errors, score);
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

