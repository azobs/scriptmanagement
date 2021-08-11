package com.sprintgether.script.management.server.scriptmanagement.exception.script;

public class PropositionNotFoundException extends Exception {
    public PropositionNotFoundException() {
    }

    public PropositionNotFoundException(String message) {
        super(message);
    }

    public PropositionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropositionNotFoundException(Throwable cause) {
        super(cause);
    }

    public PropositionNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
