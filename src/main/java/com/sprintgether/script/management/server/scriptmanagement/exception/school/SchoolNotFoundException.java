package com.sprintgether.script.management.server.scriptmanagement.exception.school;

public class SchoolNotFoundException extends Exception {
    public SchoolNotFoundException() {
    }

    public SchoolNotFoundException(String message) {
        super(message);
    }

    public SchoolNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SchoolNotFoundException(Throwable cause) {
        super(cause);
    }

    public SchoolNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
