package com.sprintgether.script.management.server.scriptmanagement.exception.script;

public class ProblemNotBelongingToPaperException extends Exception {
    public ProblemNotBelongingToPaperException() {
    }

    public ProblemNotBelongingToPaperException(String message) {
        super(message);
    }

    public ProblemNotBelongingToPaperException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProblemNotBelongingToPaperException(Throwable cause) {
        super(cause);
    }

    public ProblemNotBelongingToPaperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
