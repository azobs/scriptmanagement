package com.sprintgether.script.management.server.scriptmanagement.exception.school;

public class SectionNotFoundException extends Exception {
    public SectionNotFoundException() {
    }

    public SectionNotFoundException(String message) {
        super(message);
    }

    public SectionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SectionNotFoundException(Throwable cause) {
        super(cause);
    }

    public SectionNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
