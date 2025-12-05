package com.datavalidator.validators;

import com.datavalidator.core.ValidationResult;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RequiredFieldValidatorTest {
    @Test
    void testValidRecord() {
        RequiredFieldValidator validator = new RequiredFieldValidator(
            "test", Arrays.asList("id", "name"), 5, true
        );
        
        Map<String, Object> record = new HashMap<>();
        record.put("id", 1);
        record.put("name", "test");
        
        ValidationResult result = validator.validate(record);
        assertTrue(result.isValid());
        assertEquals(1.0, result.getQualityScore(), 0.01);
    }

    @Test
    void testMissingField() {
        RequiredFieldValidator validator = new RequiredFieldValidator(
            "test", Arrays.asList("id", "name"), 5, true
        );
        
        Map<String, Object> record = new HashMap<>();
        record.put("id", 1);
        // name missing
        
        ValidationResult result = validator.validate(record);
        assertFalse(result.isValid());
        assertEquals(1, result.getErrors().size());
    }

    @Test
    void testRelaxedMode() {
        RequiredFieldValidator validator = new RequiredFieldValidator(
            "test", Arrays.asList("id", "name"), 5, false
        );
        
        Map<String, Object> record = new HashMap<>();
        record.put("id", 1);
        // name missing
        
        ValidationResult result = validator.validate(record);
        // In relaxed mode, might still be valid if no critical errors
        assertTrue(result.getErrors().size() > 0);
    }
}

