package com.sprintgether.script.management.server.scriptmanagement.exception.script;

public class QuestionAlreadyBelongingToProblemException extends Exception {
    public QuestionAlreadyBelongingToProblemException() {
    }

    public QuestionAlreadyBelongingToProblemException(String message) {
        super(message);
    }

    public QuestionAlreadyBelongingToProblemException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuestionAlreadyBelongingToProblemException(Throwable cause) {
        super(cause);
    }

    public QuestionAlreadyBelongingToProblemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
