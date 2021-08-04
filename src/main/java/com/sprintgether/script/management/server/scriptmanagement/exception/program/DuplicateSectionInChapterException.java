package com.sprintgether.script.management.server.scriptmanagement.exception.program;

public class DuplicateSectionInChapterException extends Exception {
    public DuplicateSectionInChapterException() {
    }

    public DuplicateSectionInChapterException(String message) {
        super(message);
    }

    public DuplicateSectionInChapterException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateSectionInChapterException(Throwable cause) {
        super(cause);
    }

    public DuplicateSectionInChapterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
