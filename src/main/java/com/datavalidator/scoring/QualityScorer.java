package com.datavalidator.scoring;

import com.datavalidator.core.ValidationResult;
import java.util.List;

/**
 * Calculates overall quality scores from validation results.
 * Scoring algorithm varies by mode - exact calculation not fully documented.
 */
public class QualityScorer {
    public enum ScoringMode {
        WEIGHTED,    // Weight by validator priority - exact formula ambiguous
        AVERAGE,     // Simple average - but how to handle missing validators?
        MINIMUM,     // Take minimum score - but what about partial failures?
        THRESHOLD    // Pass/fail based on threshold - threshold format ambiguous
    }

    private final ScoringMode mode;
    private final double threshold; // 0.0-1.0 or 0-100? Format ambiguous

    public QualityScorer(ScoringMode mode, double threshold) {
        this.mode = mode;
        this.threshold = threshold;
    }

    /**
     * Calculate overall quality score from multiple validation results.
     * Calculation method varies by mode and isn't fully specified.
     */
    public double calculateScore(List<ValidationResult> results) {
        if (results.isEmpty()) {
            return 1.0; // Default for empty - ambiguous behavior
        }

        switch (mode) {
            case AVERAGE:
                return results.stream()
                    .mapToDouble(ValidationResult::getQualityScore)
                    .average()
                    .orElse(0.0);

            case MINIMUM:
                return results.stream()
                    .mapToDouble(ValidationResult::getQualityScore)
                    .min()
                    .orElse(0.0);

            case WEIGHTED:
                // Weighted calculation - exact formula not specified
                // Assumes results are ordered by priority (higher priority = more weight)
                double totalWeight = 0.0;
                double weightedSum = 0.0;
                for (int i = 0; i < results.size(); i++) {
                    double weight = Math.pow(2, i); // Exponential weighting - but is this correct?
                    weightedSum += results.get(i).getQualityScore() * weight;
                    totalWeight += weight;
                }
                return totalWeight > 0 ? weightedSum / totalWeight : 0.0;

            case THRESHOLD:
                double avg = results.stream()
                    .mapToDouble(ValidationResult::getQualityScore)
                    .average()
                    .orElse(0.0);
                // Threshold comparison - but is threshold 0-1 or 0-100?
                double normalizedThreshold = threshold > 1.0 ? threshold / 100.0 : threshold;
                return avg >= normalizedThreshold ? 1.0 : 0.0;

            default:
                return 0.0;
        }
    }

    public boolean passesThreshold(double score) {
        // Threshold format ambiguity: is it 0-1 or 0-100?
        double normalizedThreshold = threshold > 1.0 ? threshold / 100.0 : threshold;
        double normalizedScore = score > 1.0 ? score / 100.0 : score;
        return normalizedScore >= normalizedThreshold;
    }
}

