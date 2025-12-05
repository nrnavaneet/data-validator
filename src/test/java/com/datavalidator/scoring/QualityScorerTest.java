package com.datavalidator.scoring;

import com.datavalidator.core.ValidationResult;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class QualityScorerTest {
    @Test
    void testAverageMode() {
        QualityScorer scorer = new QualityScorer(QualityScorer.ScoringMode.AVERAGE, 0.8);
        
        List<ValidationResult> results = Arrays.asList(
            new ValidationResult(true, Collections.emptyList(), 0.9),
            new ValidationResult(true, Collections.emptyList(), 0.7),
            new ValidationResult(true, Collections.emptyList(), 0.8)
        );
        
        double score = scorer.calculateScore(results);
        assertEquals(0.8, score, 0.01);
    }

    @Test
    void testMinimumMode() {
        QualityScorer scorer = new QualityScorer(QualityScorer.ScoringMode.MINIMUM, 0.8);
        
        List<ValidationResult> results = Arrays.asList(
            new ValidationResult(true, Collections.emptyList(), 0.9),
            new ValidationResult(true, Collections.emptyList(), 0.5),
            new ValidationResult(true, Collections.emptyList(), 0.8)
        );
        
        double score = scorer.calculateScore(results);
        assertEquals(0.5, score, 0.01);
    }

    @Test
    void testThresholdPass() {
        QualityScorer scorer = new QualityScorer(QualityScorer.ScoringMode.THRESHOLD, 0.8);
        
        List<ValidationResult> results = Arrays.asList(
            new ValidationResult(true, Collections.emptyList(), 0.9),
            new ValidationResult(true, Collections.emptyList(), 0.85)
        );
        
        double score = scorer.calculateScore(results);
        assertTrue(scorer.passesThreshold(score));
    }
}

