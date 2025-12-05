package com.datavalidator.validators;

import com.datavalidator.core.ValidationResult;
import java.util.Map;

/**
 * Interface for data validators.
 */
public interface Validator {
    /**
     * Validate a record.
     * @param record The data record to validate
     * @return Validation result
     */
    ValidationResult validate(Map<String, Object> record);

    /**
     * Get the name of this validator.
     */
    String getName();

    /**
     * Get the priority of this validator (higher = more important).
     * Priority can be numeric or named - interpretation varies.
     */
    int getPriority();
}

