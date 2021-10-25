package com.sprintgether.script.management.server.scriptmanagement.exception.script;

public class QuestionNotBelongingToProblemException extends Exception {
    public QuestionNotBelongingToProblemException() {
    }

    public QuestionNotBelongingToProblemException(String message) {
        super(message);
    }

    public QuestionNotBelongingToProblemException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuestionNotBelongingToProblemException(Throwable cause) {
        super(cause);
    }

    public QuestionNotBelongingToProblemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
