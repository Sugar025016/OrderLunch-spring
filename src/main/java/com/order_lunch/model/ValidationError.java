package com.order_lunch.model;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class ValidationError {
    private Map<String, String> errors = new HashMap<>();
    // private String code;

    public void addFieldError(String field, String message) {
        errors.put(field, message);
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
