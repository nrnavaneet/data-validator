package com.datavalidator.core;

import com.datavalidator.config.ValidationConfig;
import com.datavalidator.scoring.QualityScorer;
import com.datavalidator.validators.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Command-line interface for data validation.
 */
public class ValidatorCLI {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: ValidatorCLI --config <config.yaml> --input <data.json>");
            System.exit(1);
        }

        String configPath = null;
        String inputPath = null;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--config") && i + 1 < args.length) {
                configPath = args[i + 1];
            } else if (args[i].equals("--input") && i + 1 < args.length) {
                inputPath = args[i + 1];
            }
        }

        if (configPath == null || inputPath == null) {
            System.err.println("Error: --config and --input are required");
            System.exit(1);
        }

        try {
            ValidationConfig config = loadConfig(configPath);
            ValidationEngine engine = createEngine(config);
            
            // Load and validate data (simplified - would need proper JSON parsing)
            System.out.println("Validating data from: " + inputPath);
            System.out.println("Using config: " + configPath);
            
            // Placeholder for actual validation
            Map<String, Object> sampleRecord = new HashMap<>();
            sampleRecord.put("id", 1);
            sampleRecord.put("name", "test");
            
            ValidationResult result = engine.validate(sampleRecord);
            System.out.println("Validation result: " + (result.isValid() ? "PASS" : "FAIL"));
            System.out.println("Quality score: " + result.getQualityScore());
            System.out.println("Errors: " + result.getErrors().size());
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static ValidationConfig loadConfig(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        return mapper.readValue(new File(path), ValidationConfig.class);
    }

    private static ValidationEngine createEngine(ValidationConfig config) {
        List<Validator> validators = new ArrayList<>();
        
        for (ValidationConfig.RuleConfig ruleConfig : config.getRules()) {
            Validator validator = createValidator(ruleConfig);
            if (validator != null) {
                validators.add(validator);
            }
        }

        ValidationConfig.ScoringConfig scoringConfig = config.getScoring();
        QualityScorer.ScoringMode scoringMode = parseScoringMode(scoringConfig.getMode());
        double threshold = scoringConfig.getThresholdAsDouble();
        QualityScorer scorer = new QualityScorer(scoringMode, threshold);

        ValidationEngine.ValidationMode mode = parseValidationMode(config.getMode());
        
        return new ValidationEngine(validators, scorer, mode);
    }

    private static Validator createValidator(ValidationConfig.RuleConfig ruleConfig) {
        String type = ruleConfig.getType();
        int priority = ruleConfig.getPriorityAsInt();

        switch (type) {
            case "required":
                return new RequiredFieldValidator(
                    ruleConfig.getName(),
                    ruleConfig.getFields(),
                    priority,
                    ruleConfig.getStrict()
                );

            case "type":
                return new TypeValidator(
                    ruleConfig.getName(),
                    ruleConfig.getTypes(),
                    priority,
                    ruleConfig.getAllowCoercion()
                );

            case "range":
                return new RangeValidator(
                    ruleConfig.getName(),
                    ruleConfig.getField(),
                    ruleConfig.getMin(),
                    ruleConfig.getMax(),
                    ruleConfig.getInclusive(),
                    priority
                );

            default:
                System.err.println("Unknown validator type: " + type);
                return null;
        }
    }

    private static QualityScorer.ScoringMode parseScoringMode(String mode) {
        if (mode == null) {
            return QualityScorer.ScoringMode.AVERAGE;
        }
        switch (mode.toLowerCase()) {
            case "weighted": return QualityScorer.ScoringMode.WEIGHTED;
            case "average": return QualityScorer.ScoringMode.AVERAGE;
            case "minimum": return QualityScorer.ScoringMode.MINIMUM;
            case "threshold": return QualityScorer.ScoringMode.THRESHOLD;
            default: return QualityScorer.ScoringMode.AVERAGE;
        }
    }

    private static ValidationEngine.ValidationMode parseValidationMode(String mode) {
        if (mode == null) {
            return ValidationEngine.ValidationMode.STRICT;
        }
        switch (mode.toLowerCase()) {
            case "strict": return ValidationEngine.ValidationMode.STRICT;
            case "relaxed": return ValidationEngine.ValidationMode.RELAXED;
            case "threshold": return ValidationEngine.ValidationMode.THRESHOLD;
            default: return ValidationEngine.ValidationMode.STRICT;
        }
    }
}

