package com.ses.salessupport.exception;

/**
 * 認証例外
 */
public class AuthenticationException extends RuntimeException {
    
    public AuthenticationException(String message) {
        super(message);
    }
    
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public AuthenticationException() {
        super("認証に失敗しました");
    }
}

