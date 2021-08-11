package com.sprintgether.script.management.server.scriptmanagement.exception.script;

public class QuestionNotBelongingToStaffException extends Exception {
    public QuestionNotBelongingToStaffException() {
    }

    public QuestionNotBelongingToStaffException(String message) {
        super(message);
    }

    public QuestionNotBelongingToStaffException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuestionNotBelongingToStaffException(Throwable cause) {
        super(cause);
    }

    public QuestionNotBelongingToStaffException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
