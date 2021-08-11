package com.sprintgether.script.management.server.scriptmanagement.exception.script;

public class IndicationAlreadyBelongingToStaffException extends Exception {
    public IndicationAlreadyBelongingToStaffException() {
    }

    public IndicationAlreadyBelongingToStaffException(String message) {
        super(message);
    }

    public IndicationAlreadyBelongingToStaffException(String message, Throwable cause) {
        super(message, cause);
    }

    public IndicationAlreadyBelongingToStaffException(Throwable cause) {
        super(cause);
    }

    public IndicationAlreadyBelongingToStaffException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
