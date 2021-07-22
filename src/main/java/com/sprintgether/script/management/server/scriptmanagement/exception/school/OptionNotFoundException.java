package com.sprintgether.script.management.server.scriptmanagement.exception.school;

public class OptionNotFoundException extends Exception {
    public OptionNotFoundException() {
    }

    public OptionNotFoundException(String message) {
        super(message);
    }

    public OptionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public OptionNotFoundException(Throwable cause) {
        super(cause);
    }

    public OptionNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
