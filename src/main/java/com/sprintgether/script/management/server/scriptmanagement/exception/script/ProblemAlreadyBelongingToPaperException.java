package com.sprintgether.script.management.server.scriptmanagement.exception.script;

public class ProblemAlreadyBelongingToPaperException extends Exception {
    public ProblemAlreadyBelongingToPaperException() {
    }

    public ProblemAlreadyBelongingToPaperException(String message) {
        super(message);
    }

    public ProblemAlreadyBelongingToPaperException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProblemAlreadyBelongingToPaperException(Throwable cause) {
        super(cause);
    }

    public ProblemAlreadyBelongingToPaperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
