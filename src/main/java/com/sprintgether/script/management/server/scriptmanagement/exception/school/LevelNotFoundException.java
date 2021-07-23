package com.sprintgether.script.management.server.scriptmanagement.exception.school;

public class LevelNotFoundException extends Exception {
    public LevelNotFoundException() {
    }

    public LevelNotFoundException(String message) {
        super(message);
    }

    public LevelNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public LevelNotFoundException(Throwable cause) {
        super(cause);
    }

    public LevelNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
