package com.sprintgether.script.management.server.scriptmanagement.exception.school;

public class SubSectionNotFoundException extends Exception {
    public SubSectionNotFoundException() {
    }

    public SubSectionNotFoundException(String message) {
        super(message);
    }

    public SubSectionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SubSectionNotFoundException(Throwable cause) {
        super(cause);
    }

    public SubSectionNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
