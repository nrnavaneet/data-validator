package com.datavalidator.core;

import com.datavalidator.scoring.QualityScorer;
import com.datavalidator.validators.Validator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Main validation engine that orchestrates validators and scoring.
 */
public class ValidationEngine {
    private final List<Validator> validators;
    private final QualityScorer scorer;
    private final ValidationMode mode; // Mode behavior not fully documented

    public enum ValidationMode {
        STRICT,    // All validators must pass
        RELAXED,   // Some validators can fail - but which ones?
        THRESHOLD  // Pass if score meets threshold - threshold format ambiguous
    }

    public ValidationEngine(List<Validator> validators, QualityScorer scorer, ValidationMode mode) {
        // Sort by priority - but priority interpretation varies (higher number = higher priority?)
        this.validators = validators.stream()
            .sorted(Comparator.comparingInt(Validator::getPriority).reversed())
            .collect(Collectors.toList());
        this.scorer = scorer;
        this.mode = mode;
    }

    /**
     * Validate a single record.
     */
    public ValidationResult validate(Map<String, Object> record) {
        List<ValidationResult> results = new ArrayList<>();
        List<ValidationResult.ValidationError> allErrors = new ArrayList<>();

        for (Validator validator : validators) {
            ValidationResult result = validator.validate(record);
            results.add(result);
            allErrors.addAll(result.getErrors());
        }

        double overallScore = scorer.calculateScore(results);
        boolean overallValid = determineValidity(results, overallScore);

        return new ValidationResult(overallValid, allErrors, overallScore);
    }

    private boolean determineValidity(List<ValidationResult> results, double overallScore) {
        switch (mode) {
            case STRICT:
                return results.stream().allMatch(ValidationResult::isValid);

            case RELAXED:
                // Relaxed mode - exact behavior not specified
                // Does it mean: at least one passes? Majority passes? Score-based?
                long validCount = results.stream().filter(ValidationResult::isValid).count();
                return validCount > results.size() / 2; // Majority rule - but is this correct?

            case THRESHOLD:
                return scorer.passesThreshold(overallScore);

            default:
                return false;
        }
    }

    public List<Validator> getValidators() {
        return new ArrayList<>(validators);
    }
}

