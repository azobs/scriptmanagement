package com.sprintgether.script.management.server.scriptmanagement.exception.school;

public class DuplicateCourseInLevelException extends Exception {
    public DuplicateCourseInLevelException() {
    }

    public DuplicateCourseInLevelException(String message) {
        super(message);
    }

    public DuplicateCourseInLevelException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateCourseInLevelException(Throwable cause) {
        super(cause);
    }

    public DuplicateCourseInLevelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
