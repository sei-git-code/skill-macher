package com.ses.salessupport.exception;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.ses.salessupport.dto.ApiResponse;

/**
 * グローバル例外ハンドラー
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * リソース未発見例外のハンドリング
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        
        logger.warn("Resource not found: {}", ex.getMessage());
        
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    /**
     * バリデーション例外のハンドリング
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            ValidationException ex, WebRequest request) {
        
        logger.warn("Validation error: {}", ex.getMessage());
        
        ApiResponse<Void> response;
        if (ex.getErrors() != null && !ex.getErrors().isEmpty()) {
            response = ApiResponse.error(ex.getMessage(), ex.getErrors());
        } else {
            response = ApiResponse.error(ex.getMessage());
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    /**
     * 認証例外のハンドリング
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(
            AuthenticationException ex, WebRequest request) {
        
        logger.warn("Authentication error: {}", ex.getMessage());
        
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    
    /**
     * ビジネスロジック例外のハンドリング
     */
    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessLogicException(
            BusinessLogicException ex, WebRequest request) {
        
        logger.warn("Business logic error: {}", ex.getMessage());
        
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    
    /**
     * バリデーションエラー（@Valid）のハンドリング
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        
        logger.warn("Validation errors: {}", errors);
        
        ApiResponse<Void> response = ApiResponse.error("バリデーションエラーが発生しました", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    /**
     * その他の例外のハンドリング
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(
            Exception ex, WebRequest request) {
        
        logger.error("Unexpected error occurred", ex);
        
        ApiResponse<Void> response = ApiResponse.error("予期しないエラーが発生しました");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    
    /**
     * IllegalArgumentExceptionのハンドリング
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        
        logger.warn("Illegal argument: {}", ex.getMessage());
        
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    /**
     * NullPointerExceptionのハンドリング
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<Void>> handleNullPointerException(
            NullPointerException ex, WebRequest request) {
        
        logger.error("Null pointer exception", ex);
        
        ApiResponse<Void> response = ApiResponse.error("データの参照エラーが発生しました");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

