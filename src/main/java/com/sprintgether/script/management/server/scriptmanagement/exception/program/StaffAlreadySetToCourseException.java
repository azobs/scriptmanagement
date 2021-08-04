package com.sprintgether.script.management.server.scriptmanagement.exception.program;

public class StaffAlreadySetToCourseException extends Exception {
    public StaffAlreadySetToCourseException() {
    }

    public StaffAlreadySetToCourseException(String message) {
        super(message);
    }

    public StaffAlreadySetToCourseException(String message, Throwable cause) {
        super(message, cause);
    }

    public StaffAlreadySetToCourseException(Throwable cause) {
        super(cause);
    }

    public StaffAlreadySetToCourseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
