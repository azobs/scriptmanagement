package com.sprintgether.script.management.server.scriptmanagement.exception.school;

public class DuplicateDepartmentInSchoolException extends Exception {
    public DuplicateDepartmentInSchoolException() {
    }

    public DuplicateDepartmentInSchoolException(String message) {
        super(message);
    }

    public DuplicateDepartmentInSchoolException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateDepartmentInSchoolException(Throwable cause) {
        super(cause);
    }

    public DuplicateDepartmentInSchoolException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
