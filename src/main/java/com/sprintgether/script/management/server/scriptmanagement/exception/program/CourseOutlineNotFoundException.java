package com.sprintgether.script.management.server.scriptmanagement.exception.program;

public class CourseOutlineNotFoundException extends Exception {
    public CourseOutlineNotFoundException() {
    }

    public CourseOutlineNotFoundException(String message) {
        super(message);
    }

    public CourseOutlineNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CourseOutlineNotFoundException(Throwable cause) {
        super(cause);
    }

    public CourseOutlineNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
