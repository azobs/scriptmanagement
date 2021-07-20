package com.sprintgether.script.management.server.scriptmanagement.exception.school;

public class DuplicateInstitutionException extends Exception {
    public DuplicateInstitutionException() {
    }

    public DuplicateInstitutionException(String message) {
        super(message);
    }

    public DuplicateInstitutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateInstitutionException(Throwable cause) {
        super(cause);
    }

    public DuplicateInstitutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
