package com.sprintgether.script.management.server.scriptmanagement.exception.script;

public class ConcernedPartNotBelongingToCourseException extends Exception {
    public ConcernedPartNotBelongingToCourseException() {
    }

    public ConcernedPartNotBelongingToCourseException(String message) {
        super(message);
    }

    public ConcernedPartNotBelongingToCourseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConcernedPartNotBelongingToCourseException(Throwable cause) {
        super(cause);
    }

    public ConcernedPartNotBelongingToCourseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
