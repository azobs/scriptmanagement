package com.sprintgether.script.management.server.scriptmanagement.exception.script;

public class ProblemAlreadyBelongingToStaffException extends Exception {
    public ProblemAlreadyBelongingToStaffException() {
    }

    public ProblemAlreadyBelongingToStaffException(String message) {
        super(message);
    }

    public ProblemAlreadyBelongingToStaffException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProblemAlreadyBelongingToStaffException(Throwable cause) {
        super(cause);
    }

    public ProblemAlreadyBelongingToStaffException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
