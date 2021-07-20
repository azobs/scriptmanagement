package com.sprintgether.script.management.server.scriptmanagement.exception.school;

public class DuplicateSchoolException extends Exception {
    public DuplicateSchoolException() {
    }

    public DuplicateSchoolException(String message) {
        super(message);
    }

    public DuplicateSchoolException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateSchoolException(Throwable cause) {
        super(cause);
    }

    public DuplicateSchoolException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
