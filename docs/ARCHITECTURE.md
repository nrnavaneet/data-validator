# Data Validator Architecture

## Overview

Data Validator is a Java-based framework for validating data quality using
configurable rules and scoring algorithms. It supports multiple validation
modes and scoring strategies.

## Core Components

### Validation Engine

The `ValidationEngine` orchestrates multiple validators and applies scoring
to produce overall quality assessments. It supports three modes:

- **Strict**: All validators must pass
- **Relaxed**: Some validators can fail (exact behavior varies)
- **Threshold**: Pass if overall score meets threshold (threshold format ambiguous)

### Validators

Validators implement the `Validator` interface and check specific aspects
of data quality:

- **RequiredFieldValidator**: Checks for required fields
- **TypeValidator**: Validates field types with optional coercion
- **RangeValidator**: Validates numeric ranges

### Quality Scoring

The `QualityScorer` calculates overall quality scores using different algorithms:

- **Average**: Simple average of validator scores
- **Minimum**: Takes the minimum score
- **Weighted**: Weighted by priority (exact formula not fully specified)
- **Threshold**: Pass/fail based on threshold (format ambiguous: 0-1 or 0-100?)

## Configuration

Validation rules are defined in YAML configuration files. The configuration
supports:

- Multiple validator types
- Priority specification (numeric or named)
- Scoring mode selection
- Threshold configuration (format ambiguous)

## Known Limitations

- Threshold format ambiguity: accepts both 0.0-1.0 and 0-100, but behavior
  may vary
- Priority interpretation: higher number = higher priority, but named priorities
  ("high", "medium", "low") mapping is not fully documented
- Relaxed mode behavior: exact criteria for passing in relaxed mode is ambiguous
- Schema evolution: mentioned in docs but not fully implemented
- Streaming validation: mentioned but implementation details about buffering
  are unclear

