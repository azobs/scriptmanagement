package com.sprintgether.script.management.server.scriptmanagement.exception.script;

public class IndicationNotBelongingToPaperException extends Exception {
    public IndicationNotBelongingToPaperException() {
    }

    public IndicationNotBelongingToPaperException(String message) {
        super(message);
    }

    public IndicationNotBelongingToPaperException(String message, Throwable cause) {
        super(message, cause);
    }

    public IndicationNotBelongingToPaperException(Throwable cause) {
        super(cause);
    }

    public IndicationNotBelongingToPaperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
