package com.sprintgether.script.management.server.scriptmanagement.exception.school;

public class DuplicateChapterInModuleException extends Exception {
    public DuplicateChapterInModuleException() {
    }

    public DuplicateChapterInModuleException(String message) {
        super(message);
    }

    public DuplicateChapterInModuleException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateChapterInModuleException(Throwable cause) {
        super(cause);
    }

    public DuplicateChapterInModuleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
