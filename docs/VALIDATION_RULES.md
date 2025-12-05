# Validation Rules Reference

## Rule Types

### Required Field Validator

Validates that specified fields are present and non-null.

**Configuration:**
```yaml
- name: required_fields
  type: required
  priority: high
  fields:
    - field1
    - field2
  strict: true
```

**Ambiguities:**
- `strict` mode behavior: Does it affect empty string handling? Null vs missing?
- Empty string validation: Is an empty string considered missing?

### Type Validator

Validates field types match expected types.

**Configuration:**
```yaml
- name: type_check
  type: type
  priority: 5
  types:
    id: integer
    name: string
  allowCoercion: false
```

**Ambiguities:**
- Coercion rules: What types can be coerced? String "123" -> integer?
- Type matching: Partial matches? "Integer" vs "int"?

### Range Validator

Validates numeric fields are within specified ranges.

**Configuration:**
```yaml
- name: age_range
  type: range
  field: age
  min: 0
  max: 150
  inclusive: true
```

**Ambiguities:**
- `inclusive` default: Is it inclusive or exclusive by default?
- Boundary handling: What happens exactly at min/max?

## Priority System

Priorities can be specified as:
- Numeric: `priority: 10` (higher = more important?)
- Named: `priority: high` (maps to numeric, but mapping not fully documented)

Priority affects:
- Validator execution order
- Weighted scoring (if enabled)
- Error severity (possibly)

## Scoring Modes

### Average
Simple average of all validator scores.

### Minimum
Takes the minimum score from all validators.

### Weighted
Weighted by priority - exact formula not fully specified.

### Threshold
Pass/fail based on threshold comparison. Threshold format ambiguous:
- Is `threshold: 0.8` interpreted as 0.8 or 80?
- Code attempts to normalize, but behavior may vary

