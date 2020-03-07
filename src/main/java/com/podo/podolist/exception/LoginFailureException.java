package com.podo.podolist.exception;

public class LoginFailureException extends RuntimeException {
    public LoginFailureException() {
        super();
    }
    public LoginFailureException(String message) {
        super(message);
    }
}
