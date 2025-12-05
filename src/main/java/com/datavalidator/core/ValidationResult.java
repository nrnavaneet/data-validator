package com.datavalidator.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Result of validating a single record.
 */
public class ValidationResult {
    private final boolean valid;
    private final List<ValidationError> errors;
    private final double qualityScore; // 0.0 to 1.0 or 0 to 100? Ambiguous in docs

    public ValidationResult(boolean valid, List<ValidationError> errors, double qualityScore) {
        this.valid = valid;
        this.errors = errors != null ? new ArrayList<>(errors) : new ArrayList<>();
        this.qualityScore = qualityScore;
    }

    public boolean isValid() {
        return valid;
    }

    public List<ValidationError> getErrors() {
        return new ArrayList<>(errors);
    }

    public double getQualityScore() {
        return qualityScore;
    }

    public static class ValidationError {
        private final String ruleName;
        private final String field;
        private final String message;
        private final Severity severity;

        public ValidationError(String ruleName, String field, String message, Severity severity) {
            this.ruleName = ruleName;
            this.field = field;
            this.message = message;
            this.severity = severity;
        }

        public String getRuleName() {
            return ruleName;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }

        public Severity getSeverity() {
            return severity;
        }
    }

    public enum Severity {
        LOW, MEDIUM, HIGH, CRITICAL
    }
}

