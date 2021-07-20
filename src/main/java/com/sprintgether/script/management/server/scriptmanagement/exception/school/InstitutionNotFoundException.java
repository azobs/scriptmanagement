package com.sprintgether.script.management.server.scriptmanagement.exception.school;

public class InstitutionNotFoundException extends Exception {
    public InstitutionNotFoundException() {
    }

    public InstitutionNotFoundException(String message) {
        super(message);
    }

    public InstitutionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstitutionNotFoundException(Throwable cause) {
        super(cause);
    }

    public InstitutionNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
