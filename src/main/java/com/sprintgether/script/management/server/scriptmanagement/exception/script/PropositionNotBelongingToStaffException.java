package com.sprintgether.script.management.server.scriptmanagement.exception.script;

public class PropositionNotBelongingToStaffException extends Exception {
    public PropositionNotBelongingToStaffException() {
    }

    public PropositionNotBelongingToStaffException(String message) {
        super(message);
    }

    public PropositionNotBelongingToStaffException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropositionNotBelongingToStaffException(Throwable cause) {
        super(cause);
    }

    public PropositionNotBelongingToStaffException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
