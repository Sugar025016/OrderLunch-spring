package com.order_lunch.model;

import lombok.Data;

@Data
// @Getter
// @Setter
// @NoArgsConstructor
public class ErrorResponse {
    // private Map<String, String> errors = new HashMap<>();

    private int code;
    private String message;
    private Boolean ok=false;

    public ErrorResponse() {
    }

    public  ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }


    // public void addFieldError(String field, String message) {
    //     errors.put(field, message);
    // }

    // public Map<String, String> getErrors() {
    //     return errors;
    // }
    
    

}
