package com.sprintgether.script.management.server.scriptmanagement.exception.program;

public class ParagraphNotFoundException extends Exception {
    public ParagraphNotFoundException() {
    }

    public ParagraphNotFoundException(String message) {
        super(message);
    }

    public ParagraphNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParagraphNotFoundException(Throwable cause) {
        super(cause);
    }

    public ParagraphNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
