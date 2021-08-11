package com.sprintgether.script.management.server.scriptmanagement.exception.script;

public class ContentNotBelongingToException extends Exception {
    public ContentNotBelongingToException() {
    }

    public ContentNotBelongingToException(String message) {
        super(message);
    }

    public ContentNotBelongingToException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContentNotBelongingToException(Throwable cause) {
        super(cause);
    }

    public ContentNotBelongingToException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
