package com.sprintgether.script.management.server.scriptmanagement.exception.script;

public class ProblemNotBelongingToStaffException extends Exception {
    public ProblemNotBelongingToStaffException() {
    }

    public ProblemNotBelongingToStaffException(String message) {
        super(message);
    }

    public ProblemNotBelongingToStaffException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProblemNotBelongingToStaffException(Throwable cause) {
        super(cause);
    }

    public ProblemNotBelongingToStaffException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
