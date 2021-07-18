package com.sprintgether.script.management.server.scriptmanagement.exception.user;

public class StaffNotFoundException extends Exception {
    public StaffNotFoundException() {
    }

    public StaffNotFoundException(String message) {
        super(message);
    }

    public StaffNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public StaffNotFoundException(Throwable cause) {
        super(cause);
    }

    public StaffNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
