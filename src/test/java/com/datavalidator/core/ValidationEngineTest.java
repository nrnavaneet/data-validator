package com.datavalidator.core;

import com.datavalidator.scoring.QualityScorer;
import com.datavalidator.validators.RequiredFieldValidator;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ValidationEngineTest {
    @Test
    void testStrictMode() {
        List<com.datavalidator.validators.Validator> validators = Arrays.asList(
            new RequiredFieldValidator("req1", Arrays.asList("id"), 5, true)
        );
        
        QualityScorer scorer = new QualityScorer(QualityScorer.ScoringMode.AVERAGE, 0.8);
        ValidationEngine engine = new ValidationEngine(
            validators, scorer, ValidationEngine.ValidationMode.STRICT
        );
        
        Map<String, Object> record = new HashMap<>();
        record.put("id", 1);
        
        ValidationResult result = engine.validate(record);
        assertTrue(result.isValid());
    }

    @Test
    void testStrictModeFailure() {
        List<com.datavalidator.validators.Validator> validators = Arrays.asList(
            new RequiredFieldValidator("req1", Arrays.asList("id", "name"), 5, true)
        );
        
        QualityScorer scorer = new QualityScorer(QualityScorer.ScoringMode.AVERAGE, 0.8);
        ValidationEngine engine = new ValidationEngine(
            validators, scorer, ValidationEngine.ValidationMode.STRICT
        );
        
        Map<String, Object> record = new HashMap<>();
        record.put("id", 1);
        // name missing
        
        ValidationResult result = engine.validate(record);
        assertFalse(result.isValid());
    }
}

