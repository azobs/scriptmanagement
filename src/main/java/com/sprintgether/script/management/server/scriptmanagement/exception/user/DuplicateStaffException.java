package com.sprintgether.script.management.server.scriptmanagement.exception.user;

public class DuplicateStaffException extends Exception {
    public DuplicateStaffException() {
    }

    public DuplicateStaffException(String message) {
        super(message);
    }

    public DuplicateStaffException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateStaffException(Throwable cause) {
        super(cause);
    }

    public DuplicateStaffException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
