package com.sprintgether.script.management.server.scriptmanagement.exception.script;

public class IndicationNotBelongingToStaffException extends Exception {
    public IndicationNotBelongingToStaffException() {
    }

    public IndicationNotBelongingToStaffException(String message) {
        super(message);
    }

    public IndicationNotBelongingToStaffException(String message, Throwable cause) {
        super(message, cause);
    }

    public IndicationNotBelongingToStaffException(Throwable cause) {
        super(cause);
    }

    public IndicationNotBelongingToStaffException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
