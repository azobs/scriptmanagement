package com.sprintgether.script.management.server.scriptmanagement.exception.script;

public class ProblemNotFoundException extends Exception {
    public ProblemNotFoundException() {
    }

    public ProblemNotFoundException(String message) {
        super(message);
    }

    public ProblemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProblemNotFoundException(Throwable cause) {
        super(cause);
    }

    public ProblemNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
