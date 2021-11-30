package com.blog.blog.exceptions;

public class NoBoardWithIdException extends RuntimeException{
    public NoBoardWithIdException() {
    }

    public NoBoardWithIdException(String message) {
        super(message);
    }

    public NoBoardWithIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoBoardWithIdException(Throwable cause) {
        super(cause);
    }

    public NoBoardWithIdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
