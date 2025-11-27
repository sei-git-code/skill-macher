package com.ses.salessupport.exception;

import java.util.List;

/**
 * バリデーション例外
 */
public class ValidationException extends RuntimeException {
    
    private List<String> errors;
    
    public ValidationException(String message) {
        super(message);
    }
    
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }
    
    public ValidationException(List<String> errors) {
        super("バリデーションエラーが発生しました");
        this.errors = errors;
    }
    
    public List<String> getErrors() {
        return errors;
    }
    
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}

