package com.sprintgether.script.management.server.scriptmanagement.exception.script;

public class PaperNotBelongingToStaffException extends Exception {
    public PaperNotBelongingToStaffException() {
    }

    public PaperNotBelongingToStaffException(String message) {
        super(message);
    }

    public PaperNotBelongingToStaffException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaperNotBelongingToStaffException(Throwable cause) {
        super(cause);
    }

    public PaperNotBelongingToStaffException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
