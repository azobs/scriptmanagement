package com.sprintgether.script.management.server.scriptmanagement.exception.school;

public class DuplicateOptionInDepartmentException extends Exception {
    public DuplicateOptionInDepartmentException() {
    }

    public DuplicateOptionInDepartmentException(String message) {
        super(message);
    }

    public DuplicateOptionInDepartmentException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateOptionInDepartmentException(Throwable cause) {
        super(cause);
    }

    public DuplicateOptionInDepartmentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
