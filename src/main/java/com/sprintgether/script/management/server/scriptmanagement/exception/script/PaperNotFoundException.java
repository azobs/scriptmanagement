package com.sprintgether.script.management.server.scriptmanagement.exception.script;

public class PaperNotFoundException extends Exception {
    public PaperNotFoundException() {
    }

    public PaperNotFoundException(String message) {
        super(message);
    }

    public PaperNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaperNotFoundException(Throwable cause) {
        super(cause);
    }

    public PaperNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
