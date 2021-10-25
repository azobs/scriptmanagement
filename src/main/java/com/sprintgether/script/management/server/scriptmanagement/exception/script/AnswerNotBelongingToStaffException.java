package com.sprintgether.script.management.server.scriptmanagement.exception.script;

public class AnswerNotBelongingToStaffException extends Exception {
    public AnswerNotBelongingToStaffException() {
    }

    public AnswerNotBelongingToStaffException(String message) {
        super(message);
    }

    public AnswerNotBelongingToStaffException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnswerNotBelongingToStaffException(Throwable cause) {
        super(cause);
    }

    public AnswerNotBelongingToStaffException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
