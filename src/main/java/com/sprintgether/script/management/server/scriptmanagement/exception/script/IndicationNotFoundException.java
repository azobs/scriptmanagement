package com.sprintgether.script.management.server.scriptmanagement.exception.script;

public class IndicationNotFoundException extends Exception {
    public IndicationNotFoundException() {
    }

    public IndicationNotFoundException(String message) {
        super(message);
    }

    public IndicationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public IndicationNotFoundException(Throwable cause) {
        super(cause);
    }

    public IndicationNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
