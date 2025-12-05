package com.datavalidator.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

/**
 * Configuration model for validation rules.
 * Some fields accept multiple formats - creating ambiguity.
 */
public class ValidationConfig {
    @JsonProperty("mode")
    private String mode; // "strict", "relaxed", "threshold"

    @JsonProperty("scoring")
    private ScoringConfig scoring;

    @JsonProperty("rules")
    private List<RuleConfig> rules;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public ScoringConfig getScoring() {
        return scoring;
    }

    public void setScoring(ScoringConfig scoring) {
        this.scoring = scoring;
    }

    public List<RuleConfig> getRules() {
        return rules;
    }

    public void setRules(List<RuleConfig> rules) {
        this.rules = rules;
    }

    public static class ScoringConfig {
        @JsonProperty("mode")
        private String mode; // "weighted", "average", "minimum", "threshold"

        @JsonProperty("threshold")
        private Object threshold; // Can be 0.0-1.0 or 0-100 - format ambiguous

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public Object getThreshold() {
            return threshold;
        }

        public void setThreshold(Object threshold) {
            this.threshold = threshold;
        }

        public double getThresholdAsDouble() {
            if (threshold instanceof Number) {
                return ((Number) threshold).doubleValue();
            } else if (threshold instanceof String) {
                return Double.parseDouble((String) threshold);
            }
            return 0.8; // Default - but is this 0.8 or 80?
        }
    }

    public static class RuleConfig {
        @JsonProperty("name")
        private String name;

        @JsonProperty("type")
        private String type; // "required", "type", "range"

        @JsonProperty("priority")
        private Object priority; // Can be int or string ("high", "medium", "low")

        @JsonProperty("fields")
        private List<String> fields;

        @JsonProperty("types")
        private Map<String, String> types;

        @JsonProperty("field")
        private String field;

        @JsonProperty("min")
        private Double min;

        @JsonProperty("max")
        private Double max;

        @JsonProperty("strict")
        private Boolean strict;

        @JsonProperty("allowCoercion")
        private Boolean allowCoercion;

        @JsonProperty("inclusive")
        private Boolean inclusive;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Object getPriority() {
            return priority;
        }

        public void setPriority(Object priority) {
            this.priority = priority;
        }

        public int getPriorityAsInt() {
            if (priority instanceof Number) {
                return ((Number) priority).intValue();
            } else if (priority instanceof String) {
                String p = ((String) priority).toLowerCase();
                switch (p) {
                    case "high": return 10;
                    case "medium": return 5;
                    case "low": return 1;
                    default: return 5;
                }
            }
            return 5; // Default priority - but interpretation varies
        }

        public List<String> getFields() {
            return fields;
        }

        public void setFields(List<String> fields) {
            this.fields = fields;
        }

        public Map<String, String> getTypes() {
            return types;
        }

        public void setTypes(Map<String, String> types) {
            this.types = types;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public Double getMin() {
            return min;
        }

        public void setMin(Double min) {
            this.min = min;
        }

        public Double getMax() {
            return max;
        }

        public void setMax(Double max) {
            this.max = max;
        }

        public Boolean getStrict() {
            return strict != null ? strict : false; // Default ambiguous
        }

        public void setStrict(Boolean strict) {
            this.strict = strict;
        }

        public Boolean getAllowCoercion() {
            return allowCoercion != null ? allowCoercion : false;
        }

        public void setAllowCoercion(Boolean allowCoercion) {
            this.allowCoercion = allowCoercion;
        }

        public Boolean getInclusive() {
            return inclusive != null ? inclusive : true; // Default ambiguous
        }

        public void setInclusive(Boolean inclusive) {
            this.inclusive = inclusive;
        }
    }
}

