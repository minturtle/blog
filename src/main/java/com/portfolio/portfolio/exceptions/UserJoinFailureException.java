package com.portfolio.portfolio.exceptions;

public class UserJoinFailureException extends RuntimeException{
    public UserJoinFailureException() {
    }

    public UserJoinFailureException(String message) {
        super(message);
    }

    public UserJoinFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserJoinFailureException(Throwable cause) {
        super(cause);
    }

    public UserJoinFailureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
