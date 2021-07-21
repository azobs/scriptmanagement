package com.sprintgether.script.management.server.scriptmanagement.exception.school;

public class SchoolExistInInstitutionException extends Exception {
    public SchoolExistInInstitutionException() {
    }

    public SchoolExistInInstitutionException(String message) {
        super(message);
    }

    public SchoolExistInInstitutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SchoolExistInInstitutionException(Throwable cause) {
        super(cause);
    }

    public SchoolExistInInstitutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
