package com.order_lunch.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class ValidationError {
    private Map<String, String> errors = new HashMap<>();
    private String code;

    public void addFieldError(String field, String message) {
        errors.put(field, message);
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    // private String code;
    // private Map<String, Integer> errors = new HashMap<>();

    // public void addFieldError(String field, int status) {
    // errors.put(field, status);
    // }

    // public Map<String, Integer> getErrors() {
    // return errors;
    // }
}
