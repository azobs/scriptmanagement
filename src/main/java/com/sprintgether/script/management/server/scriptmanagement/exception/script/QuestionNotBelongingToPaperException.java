package com.sprintgether.script.management.server.scriptmanagement.exception.script;

public class QuestionNotBelongingToPaperException extends Exception {
    public QuestionNotBelongingToPaperException() {
    }

    public QuestionNotBelongingToPaperException(String message) {
        super(message);
    }

    public QuestionNotBelongingToPaperException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuestionNotBelongingToPaperException(Throwable cause) {
        super(cause);
    }

    public QuestionNotBelongingToPaperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
