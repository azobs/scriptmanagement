package com.sprintgether.script.management.server.scriptmanagement.exception.program;

public class ChapterNotFoundException extends Exception {
    public ChapterNotFoundException() {
    }

    public ChapterNotFoundException(String message) {
        super(message);
    }

    public ChapterNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChapterNotFoundException(Throwable cause) {
        super(cause);
    }

    public ChapterNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
