package com.sprintgether.script.management.server.scriptmanagement.exception.script;

public class QuestionAlreadyBelongingToStaffException extends Exception {
    public QuestionAlreadyBelongingToStaffException() {
    }

    public QuestionAlreadyBelongingToStaffException(String message) {
        super(message);
    }

    public QuestionAlreadyBelongingToStaffException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuestionAlreadyBelongingToStaffException(Throwable cause) {
        super(cause);
    }

    public QuestionAlreadyBelongingToStaffException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
