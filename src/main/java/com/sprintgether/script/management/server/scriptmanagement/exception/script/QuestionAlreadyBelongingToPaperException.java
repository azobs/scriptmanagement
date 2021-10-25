package com.sprintgether.script.management.server.scriptmanagement.exception.script;

public class QuestionAlreadyBelongingToPaperException extends Exception {
    public QuestionAlreadyBelongingToPaperException() {
    }

    public QuestionAlreadyBelongingToPaperException(String message) {
        super(message);
    }

    public QuestionAlreadyBelongingToPaperException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuestionAlreadyBelongingToPaperException(Throwable cause) {
        super(cause);
    }

    public QuestionAlreadyBelongingToPaperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
