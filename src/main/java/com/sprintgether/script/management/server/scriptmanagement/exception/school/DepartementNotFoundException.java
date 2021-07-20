package com.sprintgether.script.management.server.scriptmanagement.exception.school;

public class DepartementNotFoundException extends Exception {
    public DepartementNotFoundException() {
    }

    public DepartementNotFoundException(String message) {
        super(message);
    }

    public DepartementNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DepartementNotFoundException(Throwable cause) {
        super(cause);
    }

    public DepartementNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
