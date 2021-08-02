package com.sprintgether.script.management.server.scriptmanagement.exception.school;

public class DuplicateSubSectionInSectionException extends Exception {
    public DuplicateSubSectionInSectionException() {
    }

    public DuplicateSubSectionInSectionException(String message) {
        super(message);
    }

    public DuplicateSubSectionInSectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateSubSectionInSectionException(Throwable cause) {
        super(cause);
    }

    public DuplicateSubSectionInSectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
