package com.sprintgether.script.management.server.scriptmanagement.exception.school;

public class SchoolNotExistInInstitutionException extends Exception {
    public SchoolNotExistInInstitutionException() {
    }

    public SchoolNotExistInInstitutionException(String message) {
        super(message);
    }

    public SchoolNotExistInInstitutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SchoolNotExistInInstitutionException(Throwable cause) {
        super(cause);
    }

    public SchoolNotExistInInstitutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
